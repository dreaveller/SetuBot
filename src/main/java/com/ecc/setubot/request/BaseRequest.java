package com.ecc.setubot.request;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class BaseRequest {
    protected String body;
    protected String accessToken;
    protected Map<String, String> header = new HashMap<>();
    protected ObjectNode param = new ObjectNode(JsonNodeFactory.instance);
}
