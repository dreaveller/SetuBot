package com.ecc.setubot.service.message.GroupMessageProcessImpl;

import com.ecc.setubot.service.message.MessageProcess;
import net.mamoe.mirai.event.events.GroupMessageEvent;

public interface GroupMessageProcess extends MessageProcess {
    boolean process(GroupMessageEvent event);
}
