package com.ecc.setubot.event;

import com.ecc.setubot.service.GroupMessageService;
import com.ecc.setubot.service.MessageFormatService;
import net.mamoe.mirai.event.*;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class GroupEvents extends SimpleListenerHost {

    private static ExecutorService executor = Executors.newFixedThreadPool(4);

    @Autowired
    GroupMessageService groupMessageService;

    @Autowired
    MessageFormatService messageFormatService;

    // 群消息判别，相应动作
    @NotNull
    @EventHandler(priority = EventPriority.NORMAL)
    public ListeningStatus groupMessageEvent(@NotNull GroupMessageEvent event) throws Exception {
        long timeLong = System.currentTimeMillis();
        executor.submit(() -> messageFormatService.saveGroupMessage(event, timeLong));
        groupMessageService.processMessage(event);
        return ListeningStatus.LISTENING;
    }

    // 新人入群欢迎
    @NotNull
    @EventHandler(priority = EventPriority.NORMAL)
    public ListeningStatus memberJoinEvent(@NotNull MemberJoinEvent event) {
        String name = event.getMember().getNick();
        event.getGroup().sendMessage("欢迎" + name);
        event.getGroup().sendMessage("群大佬 + 1，群地位 - 1");
        return ListeningStatus.LISTENING;
    }
}
