package com.ecc.setubot.task;

import com.ecc.setubot.entity.bili.BiliUserBaseInfoData;
import com.ecc.setubot.entity.bili.BiliUserBaseInfoEntity;
import com.ecc.setubot.entity.bili.LiveRoom;
import com.ecc.setubot.utils.HttpsUtils;
import com.ecc.setubot.utils.ImageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.Optional;

class BiliTaskTest {

    @Test
    public void testQueryUserBaseInfo() throws IOException {
        String url = MessageFormat.format("https://api.bilibili.com/x/space/acc/info?mid={0}", String.valueOf(672346917L));
        byte[] bytes = HttpsUtils.doGet(url, null);
        String resp = new String(bytes, "UTF-8");

        BiliUserBaseInfoEntity biliUserBaseInfoEntity = new ObjectMapper().readValue(resp, BiliUserBaseInfoEntity.class);

        Integer status = Optional.ofNullable(biliUserBaseInfoEntity)
                .map(BiliUserBaseInfoEntity::getData)
                .map(BiliUserBaseInfoData::getLiveRoom)
                .map(LiveRoom::getLiveStatus)
                .orElse(null);

        if (Objects.equals(status, null)) {
            return;
        }

        String uuu = biliUserBaseInfoEntity.getData().getLiveRoom().getCover();
        ImageUtils.downloadImage(uuu, "data/images/bili/livecover", uuu.substring(uuu.lastIndexOf("/")));
    }

//    @Test
//    public void imageDownload() {
//        ImageUtils.downloadImage()
//    }
}