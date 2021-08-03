package com.ecc.setubot.service.message;

public interface MessageProcess {
    // 本来是打算将消息类型的判断放在process的上层，利用策略模式和工厂模式实现统一接口
    // 但是消息类型过于复杂，模糊匹配，精准匹配以及@搜图匹配难度过大
    // 所以目前的实现是将消息类型的判断下沉到 process 中
}
