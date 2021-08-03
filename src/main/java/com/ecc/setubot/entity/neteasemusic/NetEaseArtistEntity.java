package com.ecc.setubot.entity.neteasemusic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NetEaseArtistEntity {
    private String name;
    private String id;
}
