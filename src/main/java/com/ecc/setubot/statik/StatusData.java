package com.ecc.setubot.statik;

import net.mamoe.mirai.message.data.Image;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class StatusData {

    public static Map<Long, Long> lastSetuTimeMillisMap = new ConcurrentHashMap<>();
    //
    public static Queue<Image> setuQueue = new ConcurrentLinkedQueue<>();


}
