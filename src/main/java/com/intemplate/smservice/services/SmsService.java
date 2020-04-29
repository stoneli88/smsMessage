package com.intemplate.smservice.services;

import com.intemplate.smservice.interceptors.GlobalInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Component
public class SmsService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private HashOperations<String, String, Object> hashOperations;

    @Resource
    private RedisService redisService;

    private static Logger logger = LoggerFactory.getLogger(GlobalInterceptor.class);

    private static final String uid = "kaiyuan66";
    public static final String key = "fd9ef3d2a8be123a62f0";
    private static final String gbk_url = "http://gbk.api.smschinesec.n/?";
    public static final String utf8_url = "http://utf8.api.smschinese.cn/?";

    private String sendMessage(String mobile, String text) {
        String sendSmsUrl = utf8_url + "Uid=" + uid + "&Key=" + key + "&smsMob=" + mobile + "&smsText=" + text;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        return restTemplate.exchange(sendSmsUrl, HttpMethod.GET, entity, String.class).getBody();
    }

    private HashMap<String, String> isExpiration(String mobile, String ip) {
        HashMap<String, String> results = new HashMap<String, String>();
        String ONE_MIN_ONE_MESSAGE = "ONE_MIN_ONE_MESSAGE" + mobile;
        String ONE_DAY_20_MESSAGE_PER_IP =  "ONE_DAY_20_MESSAGE_PER_IP" + mobile + "_" + ip;
        String THIRTY_MIN_MAX_10_MESSAGE_PER_PHONE = "THIRTY_MIN_MAX_10_MESSAGE_PER_PHONE" + mobile;

        // 发送验证码1分钟只能点击发送1次
        if (redisService.existsKey(ONE_MIN_ONE_MESSAGE + "HASH")) {
            results.put("ONE_MIN_ONE_MESSAGE", "true");
        } else {
            logger.info("Create new ONE_MIN_ONE_MESSAGE with value: " + mobile + " : " + ip);
            hashOperations.put(ONE_MIN_ONE_MESSAGE + "HASH", ONE_MIN_ONE_MESSAGE, 1);
            redisService.expireKey(ONE_MIN_ONE_MESSAGE + "HASH", 60, TimeUnit.SECONDS);
            results.put("ONE_MIN_ONE_MESSAGE", "false");
        }

        // 相同IP手机号码1天最多提交20次；
        if (redisService.existsKey(ONE_DAY_20_MESSAGE_PER_IP + "HASH")) {
            Integer count = (Integer)hashOperations.get(ONE_DAY_20_MESSAGE_PER_IP + "HASH" , ONE_DAY_20_MESSAGE_PER_IP);
            if (count > 0 && count < 20) {
                logger.info("Found exist ONE_DAY_20_MESSAGE_PER_IP with value: " + count);
                hashOperations.increment(ONE_DAY_20_MESSAGE_PER_IP + "HASH", ONE_DAY_20_MESSAGE_PER_IP, 1L);
                results.put("ONE_DAY_20_MESSAGE_PER_IP", "false");
            } else {
                results.put("ONE_DAY_20_MESSAGE_PER_IP", "true");
            }
        } else {
            hashOperations.put(ONE_DAY_20_MESSAGE_PER_IP + "HASH", ONE_DAY_20_MESSAGE_PER_IP,1L);
            results.put("ONE_DAY_20_MESSAGE_PER_IP", "false");
        }

        // 验证码短信单个手机号码30分钟最多提交10次；
        if (redisService.existsKey(THIRTY_MIN_MAX_10_MESSAGE_PER_PHONE + "HASH")) {
            Integer count = (Integer)hashOperations.get(THIRTY_MIN_MAX_10_MESSAGE_PER_PHONE + "HASH", THIRTY_MIN_MAX_10_MESSAGE_PER_PHONE);
            if (count > 0 && count < 10) {
                logger.info("Found exist THIRTY_MIN_MAX_10_MESSAGE_PER_PHONE with value: " + count);
                hashOperations.increment(THIRTY_MIN_MAX_10_MESSAGE_PER_PHONE + "HASH", ONE_DAY_20_MESSAGE_PER_IP,1L);
                results.put("THIRTY_MIN_MAX_10_MESSAGE_PER_PHONE", "false");
            } else {
                results.put("THIRTY_MIN_MAX_10_MESSAGE_PER_PHONE", "true");
            }
        } else {
            results.put("THIRTY_MIN_MAX_10_MESSAGE_PER_PHONE", "false");
            hashOperations.put(THIRTY_MIN_MAX_10_MESSAGE_PER_PHONE + "HASH", THIRTY_MIN_MAX_10_MESSAGE_PER_PHONE, 1L);
            redisService.expireKey(THIRTY_MIN_MAX_10_MESSAGE_PER_PHONE + "HASH", 30 * 60, TimeUnit.SECONDS);
        }

        return results;
    }

    public String sendMessageWithUTF8(String mobile, String text, String ip) {
        HashMap<String, String> result = this.isExpiration(mobile, ip);
        String ONE_MIN_ONE_MESSAGE = result.get("ONE_MIN_ONE_MESSAGE");
        String ONE_DAY_20_MESSAGE_PER_IP = result.get("ONE_DAY_20_MESSAGE_PER_IP");
        String THIRTY_MIN_MAX_10_MESSAGE_PER_PHONE = result.get("THIRTY_MIN_MAX_10_MESSAGE_PER_PHONE");

        if (ONE_MIN_ONE_MESSAGE.equals("true")) {
            return "一分钟只能发送一条信息";
        }
        if (ONE_DAY_20_MESSAGE_PER_IP.equals("true")) {
            return "相同IP手机号码1天最多提交20次";
        }
        if (THIRTY_MIN_MAX_10_MESSAGE_PER_PHONE.equals("true")) {
            return "验证码短信单个手机号码30分钟最多提交10次";
        }

        return "可以发送消息";
    }
}
