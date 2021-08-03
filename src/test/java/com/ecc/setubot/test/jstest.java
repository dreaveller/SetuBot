package com.ecc.setubot.test;

import org.junit.jupiter.api.Test;

import javax.script.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.text.MessageFormat;

public class jstest {

    @Test
    void jstest() throws IOException, ScriptException, NoSuchMethodException {
        // todo 待修正
        ServerSocket socket = new ServerSocket();
        socket.accept();

//        MessageChain.deserializeFromMiraiCode("1 2 [mirai:at:1584096802]   [mirai:image:{85400143-596B-BEA5-2F03-765C797EA2B8}.gif]");
    }
}
