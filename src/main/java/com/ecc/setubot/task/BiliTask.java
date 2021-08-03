package com.ecc.setubot.task;

import com.ecc.setubot.bot.MyBot;
import com.ecc.setubot.constant.BizConst;
import com.ecc.setubot.entity.bili.BiliUserBaseInfoData;
import com.ecc.setubot.entity.bili.BiliUserBaseInfoEntity;
import com.ecc.setubot.entity.bili.LiveRoom;
import com.ecc.setubot.service.BotService;
import com.ecc.setubot.service.ImageService;
import com.ecc.setubot.utils.FileUtils;
import com.ecc.setubot.utils.HttpsUtils;
import com.ecc.setubot.utils.ImageUtils;
import com.ecc.setubot.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.message.data.PlainText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

@Component
public class BiliTask {

    private static final Logger logger = LoggerFactory.getLogger(BiliTask.class);

    private static Map<Long, Integer> UID_LIVE_MAP = new HashMap<>();

    static {
        UID_LIVE_MAP.put(672346917L, 0);  // Ava
        UID_LIVE_MAP.put(672353429L, 0);  // Bella
        UID_LIVE_MAP.put(351609538L, 0);  // Carol
        UID_LIVE_MAP.put(672328094L, 0);  // Diana
        UID_LIVE_MAP.put(672342685L, 0);  // Eileen
    }

    private Iterator<Map.Entry<Long, Integer>> iterator = UID_LIVE_MAP.entrySet().iterator();

    private static final Integer LIVING = 1;
    private static final Integer OFFLINE = 0;

    private static final String BILI_LIVE_COVER_DIR = "data/images/bili/livecover/";

    @Autowired
    private ImageService imageService;

    @Autowired
    private MyBot myBot;

    @Autowired
    private BotService botService;

    @Scheduled(cron = "0/5 * * * * ?")
    public void queryUserBaseInfo() throws IOException {

        if (!iterator.hasNext()) {
            iterator = UID_LIVE_MAP.entrySet().iterator();
        }

        Map.Entry<Long, Integer> entry = iterator.next();
        String url = MessageFormat.format("https://api.bilibili.com/x/space/acc/info?mid={0}", entry.getKey().toString());
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

        if (Objects.equals(status, LIVING) && entry.getValue().equals(OFFLINE)) {
            for (Long groupID : BizConst.BETA_TEST_GROUP_ID_SET) {
                myBot.getBot().getGroup(groupID).sendMessage(buildMessageChain(biliUserBaseInfoEntity.getData()));
            }
        }

        entry.setValue(status);
    }

    private MessageChain buildMessageChain(BiliUserBaseInfoData data) throws IOException {
        Image image = downloadLiveCover(Optional.ofNullable(data.getLiveRoom()).map(LiveRoom::getCover).orElse(null));

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n" + data.getName()).append(" 直播啦!").append(data.getName() + " 可爱捏");
        stringBuilder.append("\n" + data.getLiveRoom().getTitle());
        stringBuilder.append("\n" + data.getLiveRoom().getUrl());

        return MessageUtils.newChain(image, new PlainText(stringBuilder.toString()));
    }

    private Image downloadLiveCover(String uri) throws IOException {
        if (StringUtils.isEmpty(uri)) {
            return null;
        }
        String fileName = uri.substring(uri.lastIndexOf("/"));
        if (!FileUtils.exists(BILI_LIVE_COVER_DIR + fileName)) {
            ImageUtils.downloadImage(uri, BILI_LIVE_COVER_DIR, fileName);
        }
        return botService.uploadMiraiImage(BILI_LIVE_COVER_DIR + fileName);
    }
}