package com.ecc.setubot.service.message.GroupMessageProcessImpl;

import com.ecc.setubot.constant.BizConst;
import com.ecc.setubot.service.MusicService;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderMusicProcess implements GroupMessageProcess {

    private static final String prefixString = "点歌";

    @Autowired
    private MusicService musicService;

    @Override
    public boolean process(GroupMessageEvent event) {
        String content = event.getMessage().contentToString();
        if (content.startsWith(prefixString)) {
            content = content.substring(prefixString.length());
            // content = content.replaceAll(" ", ""); todo 不可以用空格， 2022/2/28 损失了一首向晚的歌
            MessageChain messageChain = musicService.getReplyMessageChain(content);
            event.getGroup().sendMessage(messageChain);
            return BizConst.FINISH_PROCESS;
        }
        return BizConst.CONTINUE_PROCESS;
    }
}
