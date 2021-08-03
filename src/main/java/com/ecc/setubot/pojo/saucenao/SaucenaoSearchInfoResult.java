package com.ecc.setubot.pojo.saucenao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaucenaoSearchInfoResult {
    private SaucenaoSearchInfoResultHeader header;
    private SaucenaoSearchInfoResultData data;
}
