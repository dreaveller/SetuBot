package com.ecc.setubot.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// 群复读结构体
@Getter
@Setter
@AllArgsConstructor
public class GroupMessageStruct {


    String message;

    long sender;
    // 群复读计数
    int count;

}
