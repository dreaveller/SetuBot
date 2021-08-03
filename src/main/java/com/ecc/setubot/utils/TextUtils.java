package com.ecc.setubot.utils;

import java.io.UnsupportedEncodingException;

public class TextUtils {

    public static boolean isJapanese(String text) {
        try {
            return text.getBytes("shift-jis").length >= (2 * text.length());
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

}
