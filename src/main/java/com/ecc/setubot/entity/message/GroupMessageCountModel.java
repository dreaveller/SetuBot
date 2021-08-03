package com.ecc.setubot.entity.message;

import lombok.Getter;
import lombok.Setter;
import net.mamoe.mirai.message.data.MessageChain;

@Getter
@Setter
public class GroupMessageCountModel {
    private MessageChain messageChain;
    private long senderID;
    private int count;
}
