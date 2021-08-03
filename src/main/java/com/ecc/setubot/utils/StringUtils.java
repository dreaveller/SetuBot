package com.ecc.setubot.utils;

import java.util.Objects;

public class StringUtils {

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean equalsIgnoreCase(String a, String b) {
        return  (a == b) || (a != null && a.equalsIgnoreCase(b));
    }

    public static boolean equals(String a, String b) {
        return Objects.equals(a, b);
    }

    public static String trimAll(String str) {
        if (str == null)
            return "";
        return str.replaceAll(" ", "");
    }

    public static String removeStartingSpace(String original) {
        while (original.startsWith(" ")) {
            original = original.substring(1);
        }
        return original;
    }

    public static String trim(String str) {
        if (null == str)
            return "";
        return str.trim();
    }

    /**
     * 匹配ABABA句式
     * 妮可妮可妮
     *
     * @param str 需要匹配的字符串
     * @return 是否符合句式
     */
    public static boolean isABABA(String str) {
        if (isEmpty(str) || str.length() < 5) {
            return false;
        }
        char a = str.charAt(0);
        char b = str.charAt(1);
        //两个字符不能相同
        if (a == b) {
            return false;
        }
        String ababa_str = String.format("%s%s%s%s%s", a, b, a, b, a);

        return ababa_str.equalsIgnoreCase(str);
    }

    /**
     * 截取cq码中的图片链接
     *
     * @param cqStr CQ码
     * @return 图片链接
     */
    public static String getCQImageUrl(String cqStr) {
        if (null == cqStr) {
            return null;
        }
        if (!cqStr.contains("[CQ:image")) {
            return null;
        }

        String[] strs = cqStr.split(",");

        for (String str : strs) {
            if (StringUtils.isEmpty(str)) {
                continue;
            }
            if (!str.startsWith("url=")) {
                continue;
            }
            return str.substring("url=".length(), str.indexOf("]"));
        }
        return null;
    }
}
