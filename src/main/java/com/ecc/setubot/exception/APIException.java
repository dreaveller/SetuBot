package com.ecc.setubot.exception;

import lombok.Getter;

@Getter
public class APIException extends Exception {

    public String msg;

    public APIException(String msg) {
        this.msg = msg;
    }

}
