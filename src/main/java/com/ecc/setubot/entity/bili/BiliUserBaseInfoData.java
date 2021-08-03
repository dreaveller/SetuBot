package com.ecc.setubot.entity.bili;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BiliUserBaseInfoData {
    private String name;
    @JsonAlias("live_room")
    private LiveRoom liveRoom;
}
