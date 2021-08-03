package com.ecc.setubot.dao;

import com.ecc.setubot.statik.StatusData;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

@Repository
public class SetuCD {

    private static final Long coolDownTimeInterval = 300000L;

    public boolean isNotInCoolDownTime(Long senderID) {

        if (Objects.isNull(StatusData.lastSetuTimeMillisMap.get(senderID))) {
            StatusData.lastSetuTimeMillisMap.put(senderID, 0L);
        }

        Long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - StatusData.lastSetuTimeMillisMap.get(senderID) > coolDownTimeInterval) {
            StatusData.lastSetuTimeMillisMap.put(senderID, currentTimeMillis);
            return true;
        }

        return false;
    }

}
