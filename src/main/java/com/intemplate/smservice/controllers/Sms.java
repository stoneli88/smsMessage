package com.intemplate.smservice.controllers;

import com.intemplate.smservice.services.SmsService;
import com.intemplate.smservice.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class Sms {

    @Autowired
    private SmsService smsService;

    private String fillResultString(Integer status, String message, Object result) throws JSONException {
        JSONObject jsonObject = new JSONObject(){{
            put("status", status);
            put("message", message);
            put("result", result);
        }};
        return jsonObject.toString();
    }

    @RequestMapping(value = "/sms/{mobile}/{text}", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String sendMessage(@PathVariable String mobile, @PathVariable String text, HttpServletRequest request) throws JSONException {
        String returned = smsService.sendMessageWithUTF8(mobile, text, WebUtils.getIpAddr(request));
        return this.fillResultString(0, "", returned);
    }
}
