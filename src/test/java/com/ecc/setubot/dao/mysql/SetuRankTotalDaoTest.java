package com.ecc.setubot.dao.mysql;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SetuRankTotalDaoTest {

    @Autowired
    SetuRankTotalDao setuRankTotalDao;

    @Test
    void increase() {
        setuRankTotalDao.increase(2717318418L, 1);
    }

    @Test
    void getSetuCountTotal() {
        System.out.println(setuRankTotalDao.getSetuCount(1900L) == null);
    }

    @Test
    void setSetuCountTotal() {
        setuRankTotalDao.setSetuCount(1000L, 1);
    }
}