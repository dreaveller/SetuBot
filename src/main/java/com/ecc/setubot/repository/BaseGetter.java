package com.ecc.setubot.repository;

public interface BaseGetter<Response> {
    public Response doRequest();
}
