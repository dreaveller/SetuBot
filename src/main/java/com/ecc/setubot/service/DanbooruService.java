package com.ecc.setubot.service;

import com.ecc.setubot.constant.ImageConst;
import com.ecc.setubot.exception.APIException;
import com.ecc.setubot.pojo.saucenao.SaucenaoSearchInfoResult;
import com.ecc.setubot.utils.FileUtils;
import com.ecc.setubot.utils.HttpUtils;
import com.ecc.setubot.utils.ImageUtils;
import com.ecc.setubot.utils.StringUtils;
import net.mamoe.mirai.message.data.MessageChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecc.setubot.request.danbooru.DanbooruImageGet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class DanbooruService {
    private static final Logger logger = LoggerFactory.getLogger(DanbooruService.class);

    @Autowired
    private BotService botService;

    public MessageChain parseDanbooruImageRequest(SaucenaoSearchInfoResult infoResult) throws APIException {
        MessageChain result = null;

        try {
            Long danbooruID = infoResult.getData().getDanbooru_id();
            String tag = infoResult.getData().getCharacters();
            String source = infoResult.getData().getSource();
            String similarity = infoResult.getHeader().getSimilarity();

            String localImgPath = downloadImageByDanbooruID(String.valueOf(danbooruID));
            result = botService.parseMsgChainByLocalImgs(localImgPath);

            StringBuilder resultStr = new StringBuilder();
            resultStr.append("\n[相似度] ").append(similarity).append("%");
            resultStr.append("\n[DanbooruId] ").append(danbooruID);
            resultStr.append("\n[Tag] ").append(tag);
            resultStr.append("\n[来源] ").append(source);
            result.plus(resultStr.toString());

            return result;
        } catch (Exception ex) {
            logger.error("DanbooruService " + ImageConst.DANBOORU_ID_GET_ERROR_GROUP_MESSAGE + ex, ex);
            throw new APIException(ImageConst.DANBOORU_ID_GET_ERROR_GROUP_MESSAGE);
        }
    }

    /**
     * 根据 danbooruID 下载图片
     *
     * @param danbooruID danbooru 图片 ID
     * @return 本地图片路径
     */
    private String downloadImageByDanbooruID(String danbooruID) throws APIException {
        try {
            //目标页面
            DanbooruImageGet request = new DanbooruImageGet();
            request.setDanbooruId(danbooruID);
            request.doRequest();
            String imageUrl = request.getDanbooruImageUrl();

            //先检测是否已下载，如果已下载直接返回CQ，以p站图片名称为key
            String localDanbooruFilePath = ImageConst.DEFAULT_IMAGE_SAVE_PATH + File.separator + "danbooru" + File.separator + FileUtils.getFileName(imageUrl);
            if (FileUtils.exists(localDanbooruFilePath)) {
                return localDanbooruFilePath;
            }

            //下载图片
            String localUrl = ImageUtils.downloadImage(null, imageUrl, ImageConst.DEFAULT_IMAGE_SAVE_PATH + File.separator + "danbooru", null, HttpUtils.getProxy(10809));
            if (StringUtils.isNotEmpty(localUrl)) {
                return localUrl;
            }
            throw new APIException(ImageConst.DANBOORU_IMAGE_DOWNLOAD_FAIL);
        } catch (FileNotFoundException fileNotFoundEx) {
            logger.warn("DanbooruService " + ImageConst.DANBOORU_ID_GET_NOT_FOUND + "(" + danbooruID + ")");
            throw new APIException(ImageConst.DANBOORU_ID_GET_NOT_FOUND);
        } catch (IOException ioEx) {
            logger.error("DanbooruService " + ImageConst.DANBOORU_ID_GET_FAIL_GROUP_MESSAGE + "(" + danbooruID + ")", ioEx);
            throw new APIException(ImageConst.DANBOORU_ID_GET_FAIL_GROUP_MESSAGE);
        }
    }
}