package com.ecc.setubot.request.pixiv;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PixivIllustGetTest {

    @Test
    void doRequest() throws IOException {
        PixivIllustGet pixivIllustGet = new PixivIllustGet(64071270L);
        pixivIllustGet.doRequest();
        pixivIllustGet.getPixivImageInfo();
    }
}