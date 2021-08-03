package com.ecc.setubot.event;

import com.ecc.setubot.service.MessageFormatService;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.EventPriority;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FriendEvents extends SimpleListenerHost {

    @Autowired
    MessageFormatService messageParsingLocalService;

    @NotNull
    @EventHandler(priority = EventPriority.NORMAL)
    public ListeningStatus messageProcess(@NotNull FriendMessageEvent friendMessageEvent) {
        MessageChain messageChain = friendMessageEvent.getMessage();
        friendMessageEvent.getFriend().sendMessage(messageChain.plus(new PlainText("(目前只是一个复读机)")));
        return ListeningStatus.LISTENING;
    }

    @NotNull
    @EventHandler(priority = EventPriority.NORMAL)
    public ListeningStatus newFriendRequestEvent(@NotNull NewFriendRequestEvent newFriendRequestEvent) {
        newFriendRequestEvent.accept();
        return ListeningStatus.LISTENING;
    }
}
