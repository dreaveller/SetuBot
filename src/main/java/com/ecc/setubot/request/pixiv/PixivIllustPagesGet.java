package com.ecc.setubot.request.pixiv;

import com.ecc.setubot.exception.APIException;
import com.ecc.setubot.pojo.pixiv.PixivImageInfo;
import com.ecc.setubot.pojo.pixiv.PixivImageUrlInfo;
import com.ecc.setubot.utils.HttpUtils;
import com.ecc.setubot.utils.HttpsUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.Getter;
import lombok.Setter;
import com.ecc.setubot.request.BaseRequest;

import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据p站图片id获取插画图片信息 尚未找到官方API，或者稳定的第三方，所以使用爬虫
 * https://www.pixiv.net/ajax/illust/82343475/pages
 */
@Setter
@Getter
public class PixivIllustPagesGet extends BaseRequest {
    //根据pid查看多图，返回的是个常规json
    private static final String URL = "https://www.pixiv.net/ajax/illust/%s/pages";

    //就一个参数，直接构造里传入
    public PixivIllustPagesGet(Long pixivImgId) {
        pixivId = pixivImgId;
    }

    /**
     * p站图片id
     */
    private Long pixivId;

    /**
     * pixiv图片列表
     */
    private List<PixivImageUrlInfo> responseList;

    /**
     * 执行请求
     *
     * @throws IOException 所有异常上抛，由业务处理
     */
    public void doRequest() throws APIException, IOException {
        if (null == pixivId) return;
        ObjectMapper objectMapper = new ObjectMapper();
        //代理
        Proxy proxy = HttpUtils.getProxy(10809);

        //获取pid图片列表 返回的是一个标准的json文本
        byte[] resultBytes = HttpsUtils.doGet(String.format(URL, pixivId), header, proxy);
        body = new String(resultBytes);

        Map<?, ?> rootMap = objectMapper.readValue(body, HashMap.class);
        //接口调用异常
        if (null == rootMap || null == rootMap.get("error") || null == rootMap.get("body")) {
            throw new APIException("报文解析失败,body:" + body);
        }
        //接口业务异常
        if (!"false".equalsIgnoreCase(rootMap.get("error").toString())) {
            throw new APIException("接口业务失败,message:" + rootMap.get("message").toString());
        }

        //解析结果
        responseList = new ArrayList<>();
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, PixivImageInfo.class);
        List<PixivImageInfo> pixivImageInfos = objectMapper.readValue(objectMapper.writeValueAsString(rootMap.get("body")), collectionType);
        for (PixivImageInfo pixivImageInfo : pixivImageInfos) {
            responseList.add(pixivImageInfo.getUrls());
        }
    }
}