package com.ecc.setubot.request.danbooru;

import com.ecc.setubot.utils.HttpUtils;
import com.ecc.setubot.utils.HttpsUtils;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import com.ecc.setubot.request.BaseRequest;

import java.io.IOException;
import java.net.Proxy;

/**
 * 根据图片id，从danbooru获取图片信息
 */
public class DanbooruImageGet extends BaseRequest {
    //单张图片页面链接
    private static final String URL = "https://danbooru.donmai.us/posts/";

    /**
     * danbooru图片id
     */
    @Setter
    @Getter
    private String danbooruId;

    //原图链接
    @Getter
    private String danbooruImageUrl;

    public void doRequest() throws IOException {
        //通过请求获取到返回的页面
        Proxy proxy = HttpUtils.getProxy(10809);
        String htmlStr = new String(HttpsUtils.doGet(URL + danbooruId, proxy));
        //使用jsoup解析html
        Document document = Jsoup.parse(htmlStr);
        //选择目标节点，类似于JS的选择器
        Element element = document.getElementById("image-resize-link");
        String imageUrl = null;
        if (null != element) {
            //获取原图
            imageUrl = element.attributes().get("href");
        } else {
            //本身图片比较小就没有显示原图的
            imageUrl = document.getElementById("image").attributes().get("src");
        }

        danbooruImageUrl = imageUrl;
    }
}
