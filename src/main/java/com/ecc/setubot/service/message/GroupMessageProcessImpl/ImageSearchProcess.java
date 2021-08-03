package com.ecc.setubot.service.message.GroupMessageProcessImpl;

import com.ecc.setubot.bot.MyBot;
import com.ecc.setubot.constant.BizConst;
import com.ecc.setubot.service.ImageService;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.internal.message.OnlineImage;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ImageSearchProcess implements GroupMessageProcess{

    private static Logger logger = LoggerFactory.getLogger(ImageSearchProcess.class);

    @Autowired
    private ImageService imageService;

    @Autowired
    private MyBot myBot;

    @Override
    public boolean process(GroupMessageEvent event) {

        boolean isAtBot = event.getMessage()
                .stream()
                .filter(At.class::isInstance)
                .map(at -> ((At) at).getTarget())
                .anyMatch(target -> target.equals(myBot.getBot().getId()));

        if (isAtBot) {

            Image image = (Image) event.getMessage()
                    .stream()
                    .filter(Image.class::isInstance)
                    .findFirst()
                    .orElse(null);

            if (Objects.isNull(image)) {
                event.getGroup().sendMessage("@ + 图片, 搜索");
                return BizConst.FINISH_PROCESS;
            }

            try {
                String onlineUri = ((OnlineImage)image).getOriginUrl();
                event.getGroup().sendMessage(imageService.getMessageChainFromSauceNao(onlineUri));
            }  catch (Exception e) {
                e.printStackTrace();
                logger.error(e.toString());
            }
            return BizConst.FINISH_PROCESS;
        }
        return BizConst.CONTINUE_PROCESS;
    }
}
