package com.ecc.setubot;

import io.reactivex.rxjava3.core.Single;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.EventPriority;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.BotConfiguration;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

class SetubotApplicationTests {

    @Test
    void contextLoads() {
        Bot bot = BotFactory.INSTANCE.newBot(123L, "11", new BotConfiguration());
        bot.getFriend(124L).sendMessage("hello");
    }

    class repeat extends SimpleListenerHost {
        @EventHandler(priority = EventPriority.NORMAL)
        public ListeningStatus messageProcess(@NotNull FriendMessageEvent friendMessageEvent) {
            MessageChain messageChain = friendMessageEvent.getMessage();
            String string = messageChain.contentToString();
            friendMessageEvent.getFriend().sendMessage(Integer.toString(Integer.parseInt(string) + 1));
            return ListeningStatus.LISTENING;
        }
    }

    @Test
    public void funcTest() {
        Single<String> single = Single.just("1");
        single.doOnSuccess(System.out::println).map(s -> s = s + s).doOnSuccess(System.out::println).doOnError(System.out::println).blockingGet();
    }
}
