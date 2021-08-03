package com.ecc.setubot.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Setter
@Getter
public class PassTheParcelEntity {

    List<Long> playerList = new LinkedList<>();

    int countDownSecond;

    long parcelOwner;

    int muteSecond;
}
