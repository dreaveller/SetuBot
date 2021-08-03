package com.ecc.setubot.service.message.GroupMessageProcessImpl;

import com.ecc.setubot.constant.BizConst;
import com.ecc.setubot.service.WaifuService;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RandomWaifuProcess implements GroupMessageProcess {

    private static final String matchString = "随机老婆";

    @Autowired
    private WaifuService waifuService;

    @Override
    public boolean process(GroupMessageEvent event) {
        String content = event.getMessage().contentToString();
        if (content.contains(matchString) && content.length() < 7) {
            try {
                event.getGroup().sendMessage(waifuService.getRandomWaifu());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return BizConst.FINISH_PROCESS;
            }
        }
        return BizConst.CONTINUE_PROCESS;
    }
}
