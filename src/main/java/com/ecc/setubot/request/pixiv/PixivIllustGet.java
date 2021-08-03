package com.ecc.setubot.request.pixiv;

import com.ecc.setubot.constant.BizConst;
import com.ecc.setubot.pojo.pixiv.PixivImageInfo;
import com.ecc.setubot.utils.HttpsUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import com.ecc.setubot.request.BaseRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Objects;

@Setter
@Getter
public class PixivIllustGet extends BaseRequest {

    private static final String URL = "https://www.pixiv.net/artworks/";

    public PixivIllustGet(Long pixivImgId) {
        pixivId = pixivImgId;
    }

    /**
     * p站图片id
     */
    private Long pixivId;

    /**
     * pixiv图片解析后的对象
     */
    private PixivImageInfo pixivImageInfo;

    /**
     * 执行请求
     */
    public void doRequest() throws IOException {
        if (Objects.isNull(pixivId)) return;
        ObjectMapper objectMapper = new ObjectMapper();

        byte[] resultBytes = HttpsUtils.doGet(URL + pixivId, BizConst.GLOBAL_PROXY);
        body = new String(resultBytes, "utf8");

        Document document = Jsoup.parse(body);
        String pidJsonStr = document.getElementById("meta-preload-data").attr("content");
        JsonNode jsonNode = objectMapper.readTree(pidJsonStr).get("illust").get(String.valueOf(pixivId));
        pixivImageInfo = objectMapper.readValue(objectMapper.writeValueAsString(jsonNode), PixivImageInfo.class);

    }
}
