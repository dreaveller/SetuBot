package com.ecc.setubot.service;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.internal.message.OnlineImage;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MessageFormatService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean saveGroupMessage(GroupMessageEvent groupMessageEvent, long timeLong) {

        MessageChain message = groupMessageEvent.getMessage();

        // todo 先做个qq账号信息记录功能吧,这是十分必要且有用的,而且简单
        insert(
                groupMessageEvent.getGroup().getId(),
                groupMessageEvent.getSender().getId(),
                new Timestamp(timeLong),
                serialize(message)
        );

//        Long groupId = groupMessageEvent.getGroup().getId();
//        Long senderId = groupMessageEvent.getSender().getId();
//        Calendar calendar = Calendar.getInstance();
//        Integer timeStamp = groupMessageEvent.getTime();
//        List<Long> atIDList = getAtID(message);
//        List<String> picPath = new ArrayList<>();

        return true;
    }

    private void insert(Long groupID, Long senderID, Timestamp date, String text) {
        String sql = "INSERT INTO group_message (group_id, sender_id, event_time, plain_text) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, groupID, senderID, date, text);
    }

    private List<Long> getAtID(MessageChain messageChain) {
         List<Long> atIdList = messageChain.stream()
                 .filter(message -> message instanceof At)
                 .map(at -> ((At) at).getTarget())
                 .collect(Collectors.toList());
         return atIdList;
    }

    private List<String> getPicPath(MessageChain messageChain) {
        List<String> imageUriList = new ArrayList<>();
        messageChain.stream().filter(Image.class::isInstance).forEach(a -> imageUriList.add(((OnlineImage)a).getOriginUrl()));
        return new ArrayList<>();
    }

    private String serialize(MessageChain messages) {
        StringBuilder stringBuilder = new StringBuilder();
        messages.stream().forEach(message -> {
            // dispatchHandler();
            if (message instanceof PlainText) {
                stringBuilder.append(((PlainText) message).getContent());
            }
        });
        return stringBuilder.toString();
    }

    private Function<SingleMessage, String> dispatchHandler(SingleMessage message) {

        if (message instanceof PlainText) {

        } else if (message instanceof Image) {

        } else if (message instanceof At) {

        } else if (message instanceof AtAll) {

        } else if (message instanceof Face) {

        } else if (message instanceof ForwardMessage) {

        } else if (message instanceof Audio) {

        }

        return new Function<SingleMessage, String>() {
            @Override
            public String apply(SingleMessage singleMessage) {
                return null;
            }
        };
    }

    private Function<Image, String> imageHandler() {
        return image -> {
            return "";
        };
    }
}
