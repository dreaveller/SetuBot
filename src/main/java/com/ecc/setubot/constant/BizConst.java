package com.ecc.setubot.constant;

import com.ecc.setubot.utils.HttpUtils;
import com.google.common.collect.ImmutableSet;

import java.net.Proxy;
import java.util.Set;

public class BizConst {
    // 在多个process中表示继续执行
    public static final boolean CONTINUE_PROCESS = false;
    // 在多个process中表示终止执行
    public static final boolean FINISH_PROCESS = true;

    public static final Set<Long> BETA_TEST_GROUP_ID_SET = ImmutableSet.of(
            1075928069L,
            695937839L
    );

    public static final Proxy GLOBAL_PROXY = HttpUtils.getProxy(10809);
}
