package com.ecc.setubot.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class WaifuServiceTest {

    @Autowired
    WaifuService waifuService;

    @Test
    void getRandomWaifu() throws Exception {
        waifuService.getRandomWaifu();
    }
}