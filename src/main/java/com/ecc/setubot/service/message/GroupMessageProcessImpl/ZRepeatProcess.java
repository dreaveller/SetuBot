package com.ecc.setubot.service.message.GroupMessageProcessImpl;

import com.ecc.setubot.constant.BizConst;
import com.ecc.setubot.dao.GlobalGroupMessage;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ZRepeatProcess implements GroupMessageProcess {

    @Autowired
    GlobalGroupMessage globalGroupMessage;

    @Override
    public boolean process(GroupMessageEvent event) {
        if (globalGroupMessage.getPermission(event.getGroup().getId(), event.getSender().getId(), event.getMessage().contentToString())){
            event.getGroup().sendMessage(event.getMessage());
        }
        return BizConst.FINISH_PROCESS;
    }
}
