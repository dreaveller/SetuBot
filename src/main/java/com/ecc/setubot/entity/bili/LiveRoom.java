package com.ecc.setubot.entity.bili;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LiveRoom {
    private Integer roomStatus;
    private Integer liveStatus;
    private String url;
    private String title;
    private String cover;
    @JsonAlias("roomid")
    private Long roomId;
}
