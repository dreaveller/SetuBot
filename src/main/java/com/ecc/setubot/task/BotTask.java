package com.ecc.setubot.task;

import com.ecc.setubot.bot.MyBot;
import com.ecc.setubot.entity.qq.UserEntity;
import com.ecc.setubot.utils.CollectionUtils;
import net.mamoe.mirai.contact.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class BotTask {

    @Autowired
    private MyBot myBot;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Scheduled(cron = "0/1 0 0-2 * * ? ")
//    @Scheduled(cron = "0/5 * * * * ? ")
    public void updateFriendsInfo() {
        myBot.getBot().getFriends().forEach(friend -> {
            UserEntity userEntity = mapFriendEntity(friend);
            updateFriendInfo(userEntity);
        });
    }

    private void updateFriendInfo(UserEntity userEntity) {
        String selectSql = "SELECT * FROM account_info WHERE id = ?";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(selectSql, String.valueOf(userEntity.getID()));

        if (CollectionUtils.isNotEmpty(list)) {
            String updateSql = "UPDATE account_info SET id = ?, nickname = ?, avatar_url = ? WHERE id = ?";
            jdbcTemplate.update(updateSql, userEntity.getID(), userEntity.getNickName(), userEntity.getAvatarUrl(), userEntity.getID());
            return;
        }

        String insertSql = "INSERT INTO account_info (id, nickname, avatar_url) VALUES (?, ?, ?)";
        jdbcTemplate.update(insertSql, userEntity.getID(), userEntity.getNickName(), userEntity.getAvatarUrl());
        return;
    }

    private UserEntity mapFriendEntity(Friend friend) {
        UserEntity userEntity = new UserEntity();
        userEntity.setID(friend.getId());
        userEntity.setAvatarUrl(friend.getAvatarUrl());
        userEntity.setNickName(friend.getNick());
        userEntity.setRemark(friend.getRemark());
        return userEntity;
    }
}
