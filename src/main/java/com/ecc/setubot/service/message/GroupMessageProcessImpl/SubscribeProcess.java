package com.ecc.setubot.service.message.GroupMessageProcessImpl;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.springframework.stereotype.Service;

@Service
public class SubscribeProcess implements GroupMessageProcess {


    @Override
    public boolean process(GroupMessageEvent event) {

        if (event.getMessage().contentToString().startsWith("订阅")) {
            String text = event.getMessage().contentToString().substring(2);

        }

        return false;
    }
}
