package com.ecc.setubot.pojo.pixiv;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PixivImageUrlInfo {
    private String mini;
    private String thumb;
    private String small;
    private String regular;
    //原图
    private String original;
}
