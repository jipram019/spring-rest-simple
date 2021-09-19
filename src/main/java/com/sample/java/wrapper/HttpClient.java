package com.sample.java.wrapper;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class HttpClient {
    @Autowired
    HttpWrapper httpWrapper;

    public HttpClient (){}

    public String sendRequest(String host, String jsonInputString) {
        //String urlParameters  = "storeId=10001&channelId=swagger&clientId=swagger&userId=user-wh-manager&requestId=swagger";

        String urlParameters = httpWrapper.constructQueryParams();
        String endpoint = host + urlParameters;

        byte[] postData = jsonInputString.getBytes(StandardCharsets.UTF_8);
        HttpURLConnection conn = httpWrapper.createConnection(endpoint, postData.length);

        String base64String = "";
        if (httpWrapper.sendHttpRequest(conn, jsonInputString)) {
            JsonNode response = httpWrapper.readHttpResponse(conn);
            if (response != null || !response.isEmpty()) {
                base64String = response.get("data").get("bytes").asText();
            }
        }

        return base64String;
    }
}
