package com.ecc.setubot.service.message.GroupMessageProcessImpl;

import com.ecc.setubot.constant.BizConst;
import com.ecc.setubot.constant.ConfigConst;
import com.ecc.setubot.dao.SetuCD;
import com.ecc.setubot.dao.mysql.SetuRankTotalDao;
import com.ecc.setubot.service.ImageService;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

@Component
public class SetuReplyProcess implements GroupMessageProcess {

    @Autowired
    private SetuRankTotalDao setuRankTotalDao;

    @Autowired
    private SetuCD setuCD;

    @Autowired
    private ImageService imageService;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(4);

    @Override
    public boolean process(GroupMessageEvent event) {
        boolean isMatched = Pattern.matches(ConfigConst.SETU_MATCH_REGEX, event.getMessage().contentToString());
        if (!isMatched) return BizConst.CONTINUE_PROCESS;
        else {

            if (!setuCD.isNotInCoolDownTime(event.getSender().getId()) && event.getSender().getId() != ConfigConst.ADMIN_ACCOUNT) {
                event.getGroup().sendMessage(MessageUtils.newChain(new PlainText(ConfigConst.SETU_CD_RESP)));
                return BizConst.FINISH_PROCESS;
            }

            // 修复了腾讯杀图问题，并且添加了缓冲区，理论上讲现在发图应该很快，但实际效果还需测试
            // 想封装一个缓冲工具包来着，但不知如何下手，以后边学习边搞
            Image message = imageService.getImageFromBuffer();
            event.getGroup().sendMessage(message).recallIn(ConfigConst.RECALL_DELAY);

            executorService.submit(() -> {
                if (Objects.isNull(setuRankTotalDao.getSetuCount(event.getSender().getId()))) {
                    setuRankTotalDao.insert(event.getSender().getId(), 1);
                } else {
                    // 用increase是怕数据传坏了来着，有待考证
                    setuRankTotalDao.increase(event.getSender().getId(), 1);
                }
            });

            // 更新复读表
//            globalGroupMessage.setPermission(event.getGroup().getId(), message.getImageId());
            return BizConst.FINISH_PROCESS;
        }
    }
}
