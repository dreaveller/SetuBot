package com.ecc.setubot;

import com.ecc.setubot.bot.MyBot;
import com.ecc.setubot.config.CommonConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.mamoe.mirai.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class LaunchBot implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(LaunchBot.class);

    @Autowired
    @Qualifier("botEvents")
    List<ListenerHost> events;

    @Autowired
    private CommonConfig commonConfig;

    @Autowired
    private MyBot myBot;

    @Override
    public void run(ApplicationArguments args) throws IOException {

        Map<String, String> map = new ObjectMapper().readValue(new File("./config.json"), new TypeReference<Map<String, String>>() { });
        map.entrySet().forEach(entry -> {
            commonConfig.set(entry.getKey(), entry.getValue());
        });

        myBot.startBot(
                commonConfig.getBotAccount(),
                commonConfig.getBotPassword(),
                commonConfig.getBotAccount().toString() + ".json",
                events,
                commonConfig.getAdmin()
        );
    }
}
