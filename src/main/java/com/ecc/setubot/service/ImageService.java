package com.ecc.setubot.service;

import com.ecc.setubot.constant.ImageConst;
import com.ecc.setubot.exception.APIException;
import com.ecc.setubot.helper.ImageHelper;
import com.ecc.setubot.pojo.pixiv.PixivImageInfo;
import com.ecc.setubot.pojo.saucenao.SaucenaoSearchInfoResult;
import com.ecc.setubot.pojo.saucenao.SaucenaoSearchResponse;
import com.ecc.setubot.statik.StatusData;
import com.ecc.setubot.utils.CollectionUtils;
import com.ecc.setubot.utils.FileUtils;
import com.ecc.setubot.utils.NumberUtils;
import com.ecc.setubot.utils.StringUtils;
import com.google.common.collect.ImmutableMap;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.message.data.PlainText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ecc.setubot.request.saucenao.SauceNaoSearchRequest;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    private static final Map<Integer, String> sourceMap = ImmutableMap.of(
            5, "pixiv",
            9, "danbooru"
    );

    private static final int imageBufferSize = 10;

    private static ExecutorService imageSupplyExecutor = Executors.newSingleThreadExecutor();

    @Value("${saucenao.key}")
    private String saucenaoKey;

    @Autowired
    private PixivService pixivService;

    @Autowired
    private DanbooruService danbooruService;

    @Autowired
    private BotService botService;

    @Autowired
    private ImageHelper imageHelper;


    public MessageChain getMessageChainFromSauceNao(String imageUrl) {

        try {
            SaucenaoSearchInfoResult saucenaoSearchInfoResult = searchFromSauceNao(imageUrl);
            if (Objects.isNull(saucenaoSearchInfoResult)) {
                return MessageUtils.newChain(new PlainText(ImageConst.SAUCENAO_SEARCH_FAIL_PARAM));
            }

            return buildMessageChain(saucenaoSearchInfoResult);

        } catch (SocketTimeoutException e) {
            logger.error(ImageConst.SAUCENAO_API_TIMEOUT_FAIL + e, e);
            return MessageUtils.newChain(new PlainText(ImageConst.SAUCENAO_API_TIMEOUT_FAIL));
        } catch (APIException e) {
            logger.error(e.getMsg());
            return MessageUtils.newChain(new PlainText(e.getMsg()));
        } catch (IOException e) {
            logger.error(ImageConst.IMAGE_GET_TIMEOUT_ERROR + e, e);
            return MessageUtils.newChain(new PlainText(ImageConst.IMAGE_GET_TIMEOUT_ERROR));
        } catch (Exception e) {
            logger.error(ImageConst.SAUCENAO_API_REQUEST_ERROR + e, e);
            return MessageUtils.newChain(new PlainText(ImageConst.SAUCENAO_API_REQUEST_ERROR));
        }
    }

    /**
     * 搜图
     */
    private SaucenaoSearchInfoResult searchFromSauceNao(String imageUrl) throws APIException, IOException {

        if (StringUtils.isEmpty(saucenaoKey)) {
            logger.warn(ImageConst.SAUCENAO_API_KEY_EMPTY);
        }

        SauceNaoSearchRequest request = genSauceNaoSearchRequest(imageUrl);
        request.doRequest();

        SaucenaoSearchResponse response = request.getEntity();

        if (Objects.isNull(response)) {
            throw new APIException(ImageConst.SAUCENAO_API_SEARCH_FAIL);
        }

        List<SaucenaoSearchInfoResult> infoResultList = response.getResults();

        if (CollectionUtils.isEmpty(infoResultList)) {
            throw new APIException(ImageConst.SAUCENAO_SEARCH_FAIL);
        }

        SaucenaoSearchInfoResult searchResult = infoResultList.stream()
                .filter(infoResult -> Objects.nonNull(sourceMap.get(infoResult.getHeader().getIndex_id())))
                .filter(infoResult -> {
                    if (infoResult.getHeader().getIndex_id() == 9) {
                        return Objects.isNull(infoResult.getData().getDanbooru_id());
                    }
                    return true;
                })
                .filter(infoResult -> StringUtils.isNotEmpty(infoResult.getHeader().getSimilarity()))
                .filter(infoResult -> NumberUtils.toDouble(infoResult.getHeader().getSimilarity()) < 50.0)
                .findFirst()
                .orElse(null);

        return searchResult;
    }

    /**
     * 构造消息链
     */
    private MessageChain buildMessageChain(SaucenaoSearchInfoResult searchResult) throws IOException, APIException {
        switch (searchResult.getHeader().getIndex_id()) {
            case 5:
                PixivImageInfo pixivImageInfo = pixivService.getPixivImgInfoById((long) searchResult.getData().getPixiv_id());
                return pixivService.parseMessageChain(pixivImageInfo, searchResult.getHeader().getSimilarity());
            case 9:
                return danbooruService.parseDanbooruImageRequest(searchResult);
            default:
                return MessageUtils.newChain(new PlainText("未收录在 Danbooru 或 Pixiv,可能为 twitter 的图片"));
        }
    }

    /**
     * 构造请求
     */
    private SauceNaoSearchRequest genSauceNaoSearchRequest(String imgUrl) {
        SauceNaoSearchRequest request = new SauceNaoSearchRequest();
        request.setAccessToken(saucenaoKey);
        request.setNumres(6);
        request.setUrl(imgUrl);
        return request;
    }

    /**
     * 拿出一张图片
     */
    public Image getImageFromBuffer() {
        Image image = StatusData.setuQueue.poll();
        if (Objects.isNull(image)) {
            image = botService.uploadMiraiImage(getRandomFile(ImageConst.SETU_PATH));
        }
        imageSupplyExecutor.submit(new Thread(() -> afterGet()));
        return image;
    }

    /**
     * 缓冲区补充图片
     */
    private void afterGet() {
        // 可能没必要 synchronized 多线程不会出错而且提高队列补充速度
        while (StatusData.setuQueue.size() < imageBufferSize) {
            try {
                StatusData.setuQueue.offer(botService.uploadMiraiImage(imageHelper.genSafeImage(getRandomFile(ImageConst.SETU_PATH))));
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("image supplement failed", e);
            }
        }
    }

    /**
     * 随机一个文件
     */
    private File getRandomFile(String directoryPath) {
        File[] files = FileUtils.getFiles(directoryPath);
        int randInt = new Random().nextInt(files.length);
        return files[randInt];
    }
}
