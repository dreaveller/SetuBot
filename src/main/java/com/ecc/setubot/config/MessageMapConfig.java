package com.ecc.setubot.config;

import com.ecc.setubot.service.message.GroupMessageProcessImpl.GroupMessageProcess;
import com.ecc.setubot.service.message.GroupMessageProcessImpl.ImageSearchProcess;
import com.ecc.setubot.service.message.GroupMessageProcessImpl.MuteProcess;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageMapConfig {
    public static Map<String, String> regexMap = new HashMap<>();
    public static Map<String, String> exactMap = new HashMap<>();
    static {
        regexMap.put("((mirai)|(竹竹))来份*(涩|色|(she))图", "test");

        exactMap.put("随机老婆", "");
        exactMap.put("禁言", "");
        exactMap.put("","");
    }
    // ===================================================================
    @Autowired
    ImageSearchProcess imageSearchProcess;
    @Autowired
    MuteProcess muteProcess;
}
