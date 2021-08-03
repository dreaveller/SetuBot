package com.ecc.setubot.dao;

import com.ecc.setubot.entity.message.GroupMessageCountModel;
import com.ecc.setubot.pojo.GroupMessageStruct;
import net.mamoe.mirai.message.data.MessageChain;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class GlobalGroupMessage {

    // 用map暂时维护一个数据库，复读功能
    Map<Long, GroupMessageStruct> map = new HashMap<>();

    private static Map<Long, GroupMessageCountModel> groupRepeatCountMap = new ConcurrentHashMap<>();

    private final static int threshold = 2;


    public boolean get(long groupID, long senderID, MessageChain messageChain) {
        if (Objects.isNull(groupRepeatCountMap.get(groupID))) {
            GroupMessageCountModel groupMessageCountModel = new GroupMessageCountModel();
            groupMessageCountModel.setMessageChain(messageChain);
            groupMessageCountModel.setCount(1);
            groupMessageCountModel.setSenderID(senderID);
            groupRepeatCountMap.put(groupID, groupMessageCountModel);
        }

        if (System.currentTimeMillis() % 1000L < 10L) {
            return true;
        }

        GroupMessageCountModel model = groupRepeatCountMap.get(groupID);

        if (Objects.equals(messageChain, model.getMessageChain())) {
            if (model.getSenderID() == senderID) {
                return false;
            }

            model.setCount(model.getCount() + 1);
            model.setSenderID(senderID);

            if (model.getCount() < 200 && model.getCount() > threshold - 1) {
                if (System.currentTimeMillis() % 1000L < 300L) {
                    model.setCount(201);
                    return true;
                }
            }
        }

        model.setSenderID(senderID);
        model.setMessageChain(messageChain);
        model.setCount(1);

        return false;
    }


    public boolean getPermission(long id, long sender, String str) {

        GroupMessageStruct groupMessageStruct = new GroupMessageStruct(str, sender, 1);

        // 小概率主动复读
        if ((new Random().nextInt(10000) + 1) < 101 ) {
            return true;
        }

        // 判断是否为空，否则会出现空指针异常
        if (map.get(id) == null) {
            map.put(id,groupMessageStruct);
            return false;
        }

        // 复读判断部分
        // 如果此条消息与上一条不同，则直接将复读计数置为1，返回false
        if (!map.get(id).getMessage().equals(str)){
            map.put(id,groupMessageStruct);
            return false;
        }
        // 如果此条消息与上一条相同，则进一步判断
        else if (map.get(id).getMessage().equals(str)) {
            groupMessageStruct = map.get(id);
            groupMessageStruct.setCount(groupMessageStruct.getCount() + 1);
            // 如果发送者不同则进一步判断
            if (map.get(id).getSender() != sender) {
                map.put(id, groupMessageStruct);
                // 机器人已经参与复读（复读计数器大于200），返回false
                if (map.get(id).getCount() > 200) {
                    return false;
                }
                // 机器人未参与此次复读则进行复读并将复读计数置为201，返回true
                // 此处有复读概率随机数， 可以由json文件指定
                if((new Random().nextInt(10000) + 1) < 3001 && map.get(id).getCount() > 1) {
                    groupMessageStruct.setCount(201);
                    map.put(id, groupMessageStruct);
                    return true;
                }
                else return false;
            }
            // 如果发送者相同则直接返回false不做任何事
            else if (map.get(id).getSender() == sender){
                return false;
            }
        }

        return false;
    }

    public void setPermission(long id, String str) {
        long sender = 457860649L;
        GroupMessageStruct groupMessageStruct = new GroupMessageStruct(str, sender, 1);
        map.put(id, groupMessageStruct);
    }
}
