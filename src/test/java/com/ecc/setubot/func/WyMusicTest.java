package com.ecc.setubot.func;

import com.ecc.setubot.bot.MyBot;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MusicKind;
import net.mamoe.mirai.message.data.MusicShare;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WyMusicTest {

    @Autowired
    private MyBot myBot;

    @Test
    void getWYSong() {
        //assertEquals("点歌 Luxurious Overture","https://music.163.com/song?id=1374456670");
        //System.out.println(new WyMusic().GetWYSong("残酷天使的行动纲领"));
    }

    @Test
    void sendNetEaseMusic() {
        Message messageChain = new MusicShare(
                MusicKind.NeteaseCloudMusic,
                "ファッション",
                "rinahamu/Yunomi",
                "http://music.163.com/song/1338728297/?userid=324076307",
                "http://gchat.qpic.cn/gchatpic_new/2717318418/1075928069-2327837091-3EC413156A13A4D7B0B14FD1365BBE00/0?term=2",
                "http://music.163.com/song/media/outer/url?id=1338728297&userid=324076307"
        );
        myBot.getBot().getFriend(2717318418L).sendMessage(messageChain);
    }

}