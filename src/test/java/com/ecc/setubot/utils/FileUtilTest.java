package com.ecc.setubot.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class FileUtilTest {
    @Test
    public void read() throws IOException {
        System.out.println(FileUtils.read("ReadTest.json"));
    }

    @Test
    public void md5() {
        // 56f9de43c623ada9d3bdd7be80d4e4bc
        System.out.println(FileUtils.md5("457860649.json"));
    }
}
