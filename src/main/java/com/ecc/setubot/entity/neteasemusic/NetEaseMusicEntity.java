package com.ecc.setubot.entity.neteasemusic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NetEaseMusicEntity {
    private String name;
    private List<String> alias;
    private Long id;
    private NetEaseAlbumEntity album;
    private List<NetEaseArtistEntity> artists;
    private int fee;
}
