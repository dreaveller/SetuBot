package com.ecc.setubot.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MusicServiceTest {

    @Test
    void getWyMusicLink() {
        new MusicService().getReplyMessageChain("ネクストレベル");
    }

    @Test
    void test() {
        getString().toString();
    }

    private String getString() {
        return new String("hello");
    }
}