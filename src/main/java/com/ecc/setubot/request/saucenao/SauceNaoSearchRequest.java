package com.ecc.setubot.request.saucenao;

import com.ecc.setubot.pojo.saucenao.SaucenaoSearchResponse;
import com.ecc.setubot.utils.HttpUtils;
import com.ecc.setubot.utils.HttpsUtils;
import com.ecc.setubot.utils.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import com.ecc.setubot.request.BaseRequest;

import java.io.IOException;
import java.util.Map;

@Getter
@Setter
public class SauceNaoSearchRequest extends BaseRequest  {
    // 主页
    private static final String URL = "https://saucenao.com/search.php";


    /**
     * 看不懂，大概是特别的搜索条件
     * search a specific index number or all without needing to generate a bitmask.
     */
    private Integer db;
    /**
     * 接口数据格式，一般用json
     * 0=normal html
     * 1=xml api(not implemented)
     * 2=json api
     */
    private Integer output_type = 2;
    /**
     * 搜索结果数目，一般一两个就行，网页上默认6个
     */
    private Integer numres;
    /**
     * 网络图片链接
     */
    private String url;

    public void doRequest() throws IOException {
        addParam();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.convertValue(param, new TypeReference<>() {});

        byte[] resultBytes = HttpsUtils.doGet(URL + HttpUtils.parseUrlEncode(map), HttpUtils.getProxy(10809));
        body = new String(resultBytes);

        //记录接口请求与返回日志
        //logger.info(String.format("Api Request SaucenaoImageSearch,param:%s,resultBody:%s", JSONObject.toJSONString(param), body));
    }

    private void addParam() {
        param.put("api_key", accessToken);
        param.put("db", db);
        param.put("output_type", output_type);
        param.put("numres", numres);
        param.put("url", url);
    }

    //获取解析后的结果对象
    public SaucenaoSearchResponse getEntity() throws JsonProcessingException {
        if (StringUtils.isEmpty(body)) {
            return null;
        }
        return new ObjectMapper().readValue(body, SaucenaoSearchResponse.class);
    }
}
