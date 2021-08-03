package com.ecc.setubot.entity.qq;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class UserEntity {
    private long ID;
    private String nickName;
    private String avatarUrl;
    private String roamingMessage;
    private String remark;
}
