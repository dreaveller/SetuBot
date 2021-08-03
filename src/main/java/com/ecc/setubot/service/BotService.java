package com.ecc.setubot.service;

import com.ecc.setubot.bot.MyBot;
import com.ecc.setubot.utils.CollectionUtils;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class BotService {

    @Autowired
    private MyBot myBot;

    private Group group;

    /**
     * 上传图片，获取图片id
     * 重载，单条转化
     *
     * @param localImagesPath 本地图片地址
     * @return mirai图片id
     */
    public Image uploadMiraiImage(String localImagesPath) throws IOException {
        ExternalResource externalImage = ExternalResource.create(new File(localImagesPath));
        if (null == group) {
            ContactList<Group> groupList = myBot.getBot().getGroups();
            for (Group grouptemp : groupList) {
                group = grouptemp;
                break;
            }
        }
        Image image = group.uploadImage(externalImage);
        externalImage.close();
        return image;
    }

    /**
     * 上传图片，获取图片id
     * 重载，单条转化
     * @return mirai图片id
     */
    public Image uploadMiraiImage(File file) {
        ExternalResource externalImage = ExternalResource.create(file);
        if (null == group) {
            ContactList<Group> groupList = myBot.getBot().getGroups();
            for (Group grouptemp : groupList) {
                group = grouptemp;
                break;
            }
        }
        Image image = group.uploadImage(externalImage);
        try {
            externalImage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 上传图片，获取图片id
     *
     * @param localImagesPath 本地图片列表
     * @return mirai图片id列表
     */
    public List<Image> uploadMiraiImage(List<String> localImagesPath) throws IOException {
        List<Image> miraiImgList = new ArrayList<>();
        //上传并获取每张图片的id
        if (CollectionUtils.isEmpty(localImagesPath)) {
            return miraiImgList;
        }
        for (String localImagePath : localImagesPath) {
            Image tempMiraiImg = uploadMiraiImage(localImagePath);
            miraiImgList.add(tempMiraiImg);
        }
        return miraiImgList;
    }

    /**
     * 单图拼接成消息连做的代码封装方法
     *
     * @param imgInfo mirai图片
     * @return 消息链
     */
    public MessageChain parseMsgChainByImg(Image imgInfo) {
        MessageChain messageChain = MessageUtils.newChain();
        messageChain = messageChain.plus("").plus(imgInfo).plus("\n");
        return messageChain;
    }

    /**
     * 针对多张图拼接成消息连做的代码封装方法
     *
     * @param imgList mirai图片集合
     * @return 消息链
     */
    public MessageChain parseMsgChainByImgs(List<Image> imgList) {
        MessageChain messageChain = MessageUtils.newChain();
        for (Image image : imgList) {
            messageChain = messageChain.plus("").plus(image).plus("\n");
        }
        return messageChain;
    }

    /**
     * 针对本地图片路径上传并拼接成消息连做的代码封装方法
     *
     * @param localImgPath 本地图片路径
     * @return 消息链
     */
    public MessageChain parseMsgChainByLocalImgs(String localImgPath) throws IOException {
        return parseMsgChainByLocalImgs(Arrays.asList(localImgPath));
    }

    /**
     * 针对本地图片路径上传并拼接成消息连做的代码封装方法
     * 重载 批量处理
     *
     * @param localImgsPath 本地图片路径
     * @return 消息链
     */
    public MessageChain parseMsgChainByLocalImgs(List<String> localImgsPath) throws IOException {
        MessageChain messageChain = MessageUtils.newChain();
        List<Image> imageList = uploadMiraiImage(localImgsPath);
        for (Image image : imageList) {
            messageChain.plus("").plus(image).plus("\n");
        }
        return messageChain;
    }
}
