package com.ecc.setubot.service;

import com.ecc.setubot.constant.ConfigConst;
import com.ecc.setubot.entity.neteasemusic.NetEaseMusicEntity;
import com.ecc.setubot.utils.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.message.data.PlainText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MusicService {

    private static final Logger logger = LoggerFactory.getLogger(MusicService.class);

    private static final String NET_EASE_MUSIC_SEARCH_URI = "https://music.163.com/api/search/pc?&s={0}&type=1&limit=10";
    private static final String NOT_FOUND_THE_MUSIC = "没有找到该歌曲";

    @Autowired
    BotService botService;

    public MessageChain getReplyMessageChain(String name) {
        List<NetEaseMusicEntity> originalMusicList = getNetEaseMusicEntityList(name);
        List<NetEaseMusicEntity> processedMusicList = sortAndFilter(originalMusicList, name);
        return buildMessageChain(processedMusicList);
    }

    private List<NetEaseMusicEntity> getNetEaseMusicEntityList(String name) {
        try {

            String aim = MessageFormat.format(NET_EASE_MUSIC_SEARCH_URI, URLEncoder.encode(name, "utf8"));

            byte[] responseByteArray = HttpsUtils.doGet(aim);
            String responseString = new String(responseByteArray, "utf8");

            // 取key-value对的部分
            ObjectMapper objectMapper = new ObjectMapper();

            String mainJson = objectMapper.readTree(responseString).get("result").get("songs").toString();
            CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, NetEaseMusicEntity.class);
            List<NetEaseMusicEntity> musicList = objectMapper.readValue(mainJson, collectionType);

            return musicList;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
        return null;
    }

    private List<NetEaseMusicEntity> sortAndFilter(List<NetEaseMusicEntity> originalMusicList, String searchName) {
        if (CollectionUtils.isEmpty(originalMusicList)) {
            return null;
        }

        List<NetEaseMusicEntity> musicList = new ArrayList<>();
        // 多轮相似度匹配
        if (TextUtils.isJapanese(CollectionUtils.first(originalMusicList).getName()) && !TextUtils.isJapanese(searchName)) {
            musicList.add(originalMusicList.get(0));
        }
        musicList.addAll(originalMusicList.stream().filter(r -> Objects.equals(r.getName(), searchName)).collect(Collectors.toList()));
        musicList.addAll(originalMusicList.stream().filter(r -> !Objects.equals(r.getName(), searchName)).collect(Collectors.toList()));

        return musicList;

    }

    private MessageChain buildMessageChain(List<NetEaseMusicEntity> musicList) {
        MessageChain messageChain = MessageUtils.newChain();
        if (CollectionUtils.isEmpty(musicList)) {
            messageChain = messageChain.plus(new PlainText(NOT_FOUND_THE_MUSIC));
            return messageChain;
        }

        NetEaseMusicEntity music = musicList.get(0);

        try {
            String localPath = downloadAlbumPic(music.getAlbum().getPicUrl() ,ConfigConst.NET_EASE_IMAGE_SAVE_PATH);
            Image image = botService.uploadMiraiImage(localPath);
            messageChain = messageChain.plus(image);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("music pic download failed", e);
        }

        StringBuilder text = new StringBuilder("");
        if (!messageChain.isEmpty()) {
            text.append("\n");
        }

        text.append("歌曲名:").append(music.getName());
        if (CollectionUtils.isNotEmpty(music.getAlias())) {
            text.append(MessageFormat.format("({0})", CollectionUtils.first(music.getAlias())));
        }

        text.append("\n歌手:").append(Optional.ofNullable(music.getArtists()).map(r -> r.get(0)).map(r -> r.getName()).orElse("未知"));

        text.append("\n" + Optional.ofNullable(music.getAlbum()).map(r -> r.getType()).orElse("专辑") + ":")
                .append(Optional.ofNullable(music.getAlbum()).map(r -> r.getName()).orElse("未知"));

        text.append("\n链接:").append("https://music.163.com/song?id=" + music.getId());

        if (music.getFee() != 1) {
            text.append("\n下载链接").append("http://music.163.com/song/media/outer/url?id=" + music.getId() + ".mp3");
        }

        messageChain = messageChain.plus(new PlainText(text));

        return messageChain;
    }

    private String downloadAlbumPic(String url, String localDirectory) throws IOException {

        String localPath = ImageUtils.downloadImage(new HashMap<>(), url, localDirectory, null, HttpUtils.getProxy(10809));
        return localPath;
    }
}
