package com.ecc.setubot.service.message.GroupMessageProcessImpl;

import com.ecc.setubot.constant.BizConst;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MuteProcess implements GroupMessageProcess{
    @Override
    public boolean process(GroupMessageEvent event) {
        if (event.getMessage().contentToString().contains("禁言")) {
            List<Long> muteList = event.getMessage()
                    .stream()
                    .filter(At.class::isInstance)
                    .map(at -> ((At) at).getTarget())
                    .collect(Collectors.toList());
            muteMembers(muteList, event.getGroup());

            int factor = muteList.size() > 10 ? 10 : muteList.size();
            event.getGroup().get(event.getSender().getId()).mute(factor * factor * factor * factor * factor * 5);

            return BizConst.FINISH_PROCESS;
        }
        return BizConst.CONTINUE_PROCESS;
    }

    private void muteMembers(List<Long> muteList, Group group) {
        for (Long member : muteList) {
            muteMember(member, group);
        }
    }

    private void muteMember(Long Id, Group group) {
        if (!group.get(Id).isMuted()) {
            group.get(Id).mute(5);
        }
    }
}
