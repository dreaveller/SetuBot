package com.ecc.setubot.service;

import com.ecc.setubot.constant.ConstantCommon;
import com.ecc.setubot.constant.ImageConst;
import com.ecc.setubot.constant.ConstantPixiv;
import com.ecc.setubot.exception.APIException;
import com.ecc.setubot.helper.ImageHelper;
import com.ecc.setubot.pojo.pixiv.PixivImageInfo;
import com.ecc.setubot.pojo.pixiv.PixivImageUrlInfo;
import com.ecc.setubot.utils.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ecc.setubot.request.pixiv.PixivIllustGet;
import com.ecc.setubot.request.pixiv.PixivIllustPagesGet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Proxy;
import java.util.*;

@Service
public class PixivService {

    private static final Logger logger = LoggerFactory.getLogger(PixivService.class);

    private static final ObjectMapper commonMapper = new ObjectMapper();

    @Autowired
    private ImageHelper imageHelper;

    @Autowired
    private BotService botService;

    @Value("${pixiv.cookie:0}")
    private String pixivCookie;

    /**
     * @param imageInfo pixiv 接口返回的图片信息实体
     */
    public MessageChain parseMessageChain(PixivImageInfo imageInfo, String similarity) throws IOException {
        MessageChain result = MessageUtils.newChain();

        // todo 原本有开关控制
        if (Objects.equals(imageInfo.getXRestrict(), 1)) {
            // 1 代表 R18
            result = result.plus(ConstantPixiv.PIXIV_IMAGE_R18);
        } else {
            parseImages(imageInfo);
            List<Image> miraiImageList = botService.uploadMiraiImage(imageInfo.getLocalImgPathList());
            result = botService.parseMsgChainByImgs(miraiImageList);
        }

        StringBuilder resultStr = new StringBuilder();
        if (imageInfo.getPageCount() > 1) {
            resultStr.append("\n该Pid包含多张图片");
        }
        resultStr.append("\n[相似度] ").append(similarity).append("%");
        resultStr.append("\n[P站ID] ").append(imageInfo.getId());
        resultStr.append("\n[标题] ").append(imageInfo.getTitle());
        resultStr.append("\n[作者] ").append(imageInfo.getUserName());
        resultStr.append("\n[上传时间] ").append(imageInfo.getCreateDate());
        result = result.plus(resultStr.toString());
        return result;
    }

    public List<String> downloadImage(PixivImageInfo imageInfo) throws IOException {
        Long pixivID = Long.valueOf(imageInfo.getId());

        List<String> localImagesPathList = new ArrayList<>();
        if (imageInfo.getPageCount() > 1) {
            try {
                localImagesPathList.addAll(downloadPixivImages(pixivID));
            } catch (FileNotFoundException e) {
                logger.warn("pixiv多图获取失败，可能登录过期,imageInfo:{}", new ObjectMapper().writeValueAsString(imageInfo), e);
                // 限制级会要求必须登录，如果不登录会抛出异常
                localImagesPathList.add(downloadPixivImage(imageInfo.getUrls().getOriginal(), pixivID));
            }
        } else {
            localImagesPathList.add(downloadPixivImage(imageInfo.getUrls().getOriginal(), pixivID));
        }
        return localImagesPathList;
    }

    /**
     * 下载图片到本地
     */
    public void parseImages(PixivImageInfo imageInfo) throws IOException {
        Long pixivID = NumberUtils.toLong(imageInfo.getId());
        List<String> localImagesPathList = new ArrayList<>();

        if (imageInfo.getPageCount() > 1) {
            //多图 如果因为没登录而获取不到，则只取封面
            try {
                localImagesPathList.addAll(downloadPixivImages(pixivID));
            } catch (FileNotFoundException e) {
                logger.warn("pixiv多图获取失败，可能登录过期,imageInfo:{}", new ObjectMapper().writeValueAsString(imageInfo), e);
                //限制级会要求必须登录，如果不登录会抛出异常
                localImagesPathList.add(downloadPixivImage(imageInfo.getUrls().getOriginal(), pixivID));
            }
        } else {
            localImagesPathList.add(downloadPixivImage(imageInfo.getUrls().getOriginal(), pixivID));
        }
        imageInfo.setLocalImgPathList(localImagesPathList);
    }

    /**
     * 下载P站图片
     */
    public String downloadPixivImage(String url, Long pixivId) throws IOException {
        //先检测是否已下载，如果已下载直接送去压图
        String pixivImgFileName = url.substring(url.lastIndexOf("/") + 1);
        String localPixivFilePath = ImageConst.DEFAULT_IMAGE_SAVE_PATH + File.separator + "pixiv" + File.separator + pixivImgFileName;
        if (FileUtils.exists(localPixivFilePath)) {
            return imageHelper.genScaledImage(localPixivFilePath);
        }

        //是否不加载p站图片，由于从p站本体拉数据，还没代理，很慢
        String pixiv_image_ignore = ConstantCommon.common_config.get(ConstantPixiv.PIXIV_CONFIG_IMAGE_IGNORE);
        if ("1".equalsIgnoreCase(pixiv_image_ignore)) {
            return null;
        }

        String scaleForceLocalUrl = null;
        try {
            String localUrl = download(url, pixivId);

            if (StringUtils.isNotEmpty(localUrl)) {
                scaleForceLocalUrl = imageHelper.genScaledImage(localUrl);
            }
            if (StringUtils.isEmpty(scaleForceLocalUrl)) {
                //图片下载或压缩失败
                logger.warn(String.format("PixivImjadService downloadPixivImg %s url:%s", ConstantPixiv.PIXIV_IMAGE_DOWNLOAD_FAIL, url));
            }
        } catch (FileNotFoundException fileNotFoundEx) {
            //图片被删了
            logger.warn(String.format("PixivImjadService downloadPixivImg %s pixivId:%s", ConstantPixiv.PIXIV_IMAGE_DELETE, pixivId));
        }
        return scaleForceLocalUrl;
    }

    /**
     * 下载并压缩P站图片(多图)
     *
     * @return 处理后的本地图片地址列表
     */
    public List<String> downloadPixivImages(Long pixivId) throws IOException {
        List<String> localImagesPath = new ArrayList<>();

        // 查看多图展示数量配置，默认为3
        String imagesShowCount = ConstantCommon.common_config.get(ConstantPixiv.PIXIV_CONFIG_IMAGES_SHOW_COUNT);
        if (!NumberUtils.isNumberOnly(imagesShowCount)) {
            imagesShowCount = ConstantPixiv.PIXIV_CONFIG_IMAGES_SHOW_COUNT_DEFAULT;
        }
        Integer showCount = NumberUtils.toInt(imagesShowCount);

        // 构建请求头并且进行请求，获取pixiv多图
        PixivIllustPagesGet request = new PixivIllustPagesGet(pixivId);
        try {
            //如果是登录可见图片，必须附带cookie，不然会抛出404异常
            Map<String, String> header = new HashMap<>();
            header.put("cookie", pixivCookie);
            request.setHeader(header);
            request.doRequest();
        } catch (APIException e) {
            logger.warn("Pixiv downloadPixivImgs apierr msg:{}", e.getMessage(), e);
            return new ArrayList<>();
        }
        List<PixivImageUrlInfo> urlInfoList = request.getResponseList();

        urlInfoList.stream().limit(showCount).forEach(urlInfo -> {
            try {
                String scaleForceLocalUrl = downloadPixivImage(urlInfo.getOriginal(), pixivId);
                localImagesPath.add(scaleForceLocalUrl);
            } catch (IOException e) {
                logger.error("Pixiv downloadPixivImgs apierr msg:{0}", e);
                e.printStackTrace();
            }
        });

        return localImagesPath;
    }

    /**
     * 查询p站图片id并返回结果
     */
    public PixivImageInfo getPixivImgInfoById(Long pid) throws IOException {
        if (Objects.isNull(pid)) {
            return null;
        }
        PixivIllustGet request = new PixivIllustGet(pid);
        request.doRequest();
        return request.getPixivImageInfo();
    }

    /**
     * 根据p站图片链接下载图片
     * https://i.pximg.net/img-original/img/2018/03/31/01/10/08/67994735_p0.png
     * @param url     p站图片链接
     * @param pixivID p站图片id，用于防爬链，必须跟url中的id一致
     * @return 下载后的本地连接
     */
    public String download(String url, Long pixivID) throws IOException {
        logger.info("Pixiv image download:" + url);
        //加入p站防爬链
        //目前一共遇到的
        //1.似乎是新连接，最近UI改了 https://i.pximg.net/img-original/img/2020/02/17/22/07/00/79561788_p0.jpg
        //Referer: https://www.pixiv.net/artworks/79561788
        //2.没研究出来的链接，还是403，但是把域名替换成正常链接的域名，可以正常获取到数据 https://i-cf.pximg.net/img-original/img/2020/02/17/22/07/00/79561788_p0.jpg
        HashMap<String, String> header = new HashMap<>();
        url = url.replace("i-cf.pximg.net", "i.pximg.net");
        header.put("referer", "https://www.pixiv.net/artworks/" + pixivID);
        // 创建代理
        Proxy proxy = HttpUtils.getProxy(10809);
        String localDir = ImageConst.DEFAULT_IMAGE_SAVE_PATH + File.separator + "pixiv";
        return ImageUtils.downloadImage(header, url, localDir, null, proxy);
    }
}
