package com.ecc.setubot.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

    /**
     * 匹配正则，是否符合
     *
     * @param regexStr 需要进行匹配的字符串
     * @param regex    正则表达式
     * @return 是否符合正则表达式
     */
    public static boolean regex(String regexStr, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(regexStr);
        return matcher.find();
    }

}
