package com.ecc.setubot.utils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CollectionUtils {

    public static boolean isNotEmpty(List<?> list) {
        return null != list && list.size() > 0;
    }

    public static boolean isEmpty(List<?> list) {
        return null == list || list.size() <= 0;
    }

    public static <T> T first(Collection<T> collection) {
        if (Objects.isNull(collection) || collection.isEmpty()) {
            return null;
        }
        return collection.iterator().next();
    }
}
