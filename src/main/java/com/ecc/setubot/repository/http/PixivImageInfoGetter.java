package com.ecc.setubot.repository.http;

import com.ecc.setubot.pojo.pixiv.PixivImageInfo;
import com.ecc.setubot.repository.BaseGetter;

public class PixivImageInfoGetter implements BaseGetter<PixivImageInfo> {

    private static final String url = "https://www.pixiv.net/artworks/{0}";

    @Override
    public PixivImageInfo doRequest() {
        return null;
    }
}
