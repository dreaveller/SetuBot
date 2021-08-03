package com.ecc.setubot.entity;

import lombok.Getter;
import lombok.Setter;
import net.mamoe.mirai.message.data.Message;

import java.util.Calendar;
import java.util.List;

@Getter
@Setter
public class GroupMessageEntity {
    private Long groupId;
    private Long senderId;
    private Message message;
    private Calendar sendDate;
    private Integer timeStamp;
    private Integer repeatCount;
    private List<Long> atId;
    private List<String> picPath;

    private String identityCode;
}
