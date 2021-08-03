package com.ecc.setubot.service;

import com.ecc.setubot.service.message.GroupMessageProcessImpl.GroupMessageProcess;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GroupMessageService {

    @Autowired
    GroupMessageProcess[] groupMessageProcesses;

    public void processMessage(GroupMessageEvent event) {
        for (GroupMessageProcess process : groupMessageProcesses) {
            if (process.process(event))
                break;
        }
    }

}
