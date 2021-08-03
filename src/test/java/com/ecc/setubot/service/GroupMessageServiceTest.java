package com.ecc.setubot.service;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.apache.catalina.Executor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GroupMessageServiceTest {

    @Autowired
    GroupMessageService groupMessageService;
    @Test
    void setuSend() {
        //groupMessageService.SetuSend();
        InputStreamReader inputStreamReader;
        InputStream inputStream;
        FileInputStream fileInputStream;
        ThreadLocal<String> threadLocal;
        //ExecutorService executorService =
    }
    @Test
    void ttt() {
        // groupMessageService.processMessage();
    }
}