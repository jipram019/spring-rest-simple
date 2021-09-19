package com.sample.java.wrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.java.controller.ApiType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpWrapper {
    @Autowired
    public ObjectMapper objectMapper;

    public HttpWrapper(){}

    public HttpURLConnection createConnection(String endpoint, int dataLen){
        URL url = null;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            log.error("Cannot form URL from string endpoint", e);
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            log.error("Cannot create connection from string endpoint", e);
        }

        conn.setDoOutput( true );
        conn.setUseCaches( false );
        conn.setInstanceFollowRedirects( false );
        try {
            conn.setRequestMethod( "POST" );
        } catch (ProtocolException e) {
            log.error("Protocol not supported", e);
        }

        conn.setRequestProperty( "Content-Type", "application/json");
        conn.setRequestProperty( "accept", "application/json");
        conn.setRequestProperty( "charset", "utf-8");
        conn.setRequestProperty( "Content-Length", Integer.toString( dataLen ));
        conn.setRequestProperty( "Connection", "keep-alive");

        return conn;
    }

    public String constructQueryParams() {
        Map<String, String> map = new HashMap<>();
        map.put("storeId", "10001");
        map.put("clientId", "swagger");
        map.put("channelId", "swagger");
        map.put("requestId", "swagger");
        map.put("userId", "user-wh-manager");

        StringBuilder params = new StringBuilder();
        for(Map.Entry entry : map.entrySet()){
            try {
                params.append(URLEncoder.encode(entry.getKey().toString(), "UTF-8"));
                params.append("=");
                params.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                params.append("&");
            } catch (UnsupportedEncodingException e) {
                log.error("Encoding params not supported", e);
            }

        }
        return params.toString();
    }

    public boolean sendHttpRequest(HttpURLConnection connection, String payload){
        byte[] postData = payload.getBytes( StandardCharsets.UTF_8 );
        try(OutputStream os = connection.getOutputStream())
        {
            os.write(postData, 0, postData.length);
        } catch (IOException e) {
            log.error("Error writing to output stream", e);
            return false;
        }
        return true;
    }

    public JsonNode readHttpResponse(HttpURLConnection connection){
        String jsonResponse = null;
        try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8")))
        {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            jsonResponse = response.toString();
        } catch (UnsupportedEncodingException e) {
            log.error("Encoding error", e);
        } catch (IOException e) {
            log.error("Error while reading http response", e);
        }

        JsonNode node = null;
        try {
            node = objectMapper.readTree(jsonResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return node;
    }

    public HttpHeaders setHttpHeaders(ApiType apiType){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        if(ApiType.PDF_GENERATOR.equals(apiType)){
            String downloadDate = new SimpleDateFormat("YYYYMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String shippingLabel = "SHIPPING_LABEL_" + downloadDate + ".pdf";
            headers.add("Content-Disposition", "attachment; filename=" + shippingLabel);
        }
        return headers;
    }
}
