package com.ecc.setubot.bot;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyBot {

    private Bot miraiBot;

    public Bot getBot() {
        return miraiBot;
    }

    public void startBot(Long account, String pwd, String deviceInfo, List<ListenerHost> events, Long admin) {

        BotConfiguration config = new BotConfiguration();
        config.fileBasedDeviceInfo(deviceInfo);
        config.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PHONE);
        config.redirectBotLogToDirectory();


        miraiBot = BotFactory.INSTANCE.newBot(account, pwd, config);
        miraiBot.login();

        // 注册事件
        for (ListenerHost event : events) {
            miraiBot.getEventChannel().registerListenerHost(event);
        }

        //admin 提示
        miraiBot.getFriend(admin).sendMessage("已上线 ！");

        // 这个和picbotx 还是不太一样 那个不会占用主线程
        // 这里必须要启新线程去跑bot 不然会占用主线程
        new Thread(() -> miraiBot.join()).start();
    }
}
