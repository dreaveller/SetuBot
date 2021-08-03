package com.ecc.setubot.config;

import com.ecc.setubot.event.FriendEvents;
import com.ecc.setubot.event.GroupEvents;
import net.mamoe.mirai.event.ListenerHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class EventsConfig {

    @Autowired
    GroupEvents groupEvents;

    @Autowired
    FriendEvents friendEvents;

    @Bean(name = "botEvents")
    public List<ListenerHost> getBotevents() {
        List<ListenerHost> events = new ArrayList<>();
        events.add(groupEvents);
        events.add(friendEvents);
        return events;
    }
}
