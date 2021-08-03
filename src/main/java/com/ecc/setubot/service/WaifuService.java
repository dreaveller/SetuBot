package com.ecc.setubot.service;

import com.ecc.setubot.utils.FileUtils;
import com.ecc.setubot.utils.StringUtils;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class WaifuService {

    @Autowired
    public BotService botService;

    public MessageChain getRandomWaifu() throws Exception {

        List<String> subPicNameList = this.getSubPicNameList();
        BufferedImage bufferedImage = this.coverImage(subPicNameList,0, 0,600 ,600);

        String filename = "data" + File.separator + "images" + File.separator + "picrewsend" + File.separator + System.currentTimeMillis() + ".png";
        ImageIO.write(bufferedImage,"png", new File(filename));

        net.mamoe.mirai.message.data.Image list = botService.uploadMiraiImage(filename);

        return MessageUtils.newChain(list);
    }

    private List<String> getSubPicNameList() {

        List<String> subPicNameList = new ArrayList<>(30);
        for (int i = 0; i < 30; i++) {
            subPicNameList.add("");
        }

        File[] files = FileUtils.getFiles("data" + File.separator + "images" + File.separator + "picrew");

        for (int i = 0; i < files.length; i++) {
            // 文件夹或者文件
            File[] element = FileUtils.getFiles(files[i].getPath());
            int randInt = new Random().nextInt(element.length);

            // 文件夹
            if (element[randInt].isDirectory()) {
                File[] files3 = FileUtils.getFiles(element[randInt].getPath());
                for (File ff : files3) {
                    String[] tmp = ff.getPath().split("_");
                    subPicNameList.set(Integer.parseInt(tmp[tmp.length - 1].split("\\.")[0]) - 1, ff.getPath());
                }
            }
            // 文件
            else {
                String[] tmp = element[randInt].getName().split("_");
                subPicNameList.set(Integer.parseInt(tmp[tmp.length - 1].split("\\.")[0]) - 1, element[randInt].getPath());
            }
        }
        return subPicNameList;
    }

    private BufferedImage coverImage(List<String> subImageList, int x, int y, int width, int height) throws IOException {

        BufferedImage buffImg = ImageIO.read(new File(subImageList.get(0)));

        for (int i = 1; i < subImageList.size() - 1; i++) {
            if (StringUtils.isEmpty(subImageList.get(i))) {
                continue;
            }
            BufferedImage coverImg = ImageIO.read(new File(subImageList.get(i)));
            buffImg = coverImage(buffImg, coverImg, x, y, width, height);
        }

        return buffImg;
    }

    private BufferedImage coverImage(BufferedImage baseBufferedImage, BufferedImage coverBufferedImage, int x, int y, int width, int height) {

        // 创建Graphics2D对象，用在底图对象上绘图
        Graphics2D g2d = baseBufferedImage.createGraphics();

        // 绘制
        g2d.drawImage(coverBufferedImage, x, y, 600, 600, null);
        g2d.dispose();// 释放图形上下文使用的系统资源

        return baseBufferedImage;
    }
}
