package com.intemplate.smservice.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class smsService {
    private static final String uid = "kaiyuan66";
    public static final String key = "fd9ef3d2a8be123a62f0";
    private static final String url = "http://gbk.api.smschinese.cn/?";


    public static void sendMessage(String mobile, String text) {
        String sendSmsUrl = url + "Uid=" + uid + "&Key=" + key + "&smsMob=" + mobile + "&smsText=" + text;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String body = restTemplate.exchange(sendSmsUrl, HttpMethod.GET, entity, String.class).getBody();
    }
}
