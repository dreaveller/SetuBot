package com.ecc.setubot.entity.bili;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BiliUserBaseInfoEntity {
    private Integer code;
    private BiliUserBaseInfoData data;
}
