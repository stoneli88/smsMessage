package com.intemplate.smservice.controllers;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class User {
    private String fillResultString(Integer status, String message, Object result) throws JSONException {
        JSONObject jsonObject = new JSONObject(){{
            put("status", status);
            put("message", message);
            put("result", result);
        }};
        return jsonObject.toString();
    }

    @RequestMapping(value = "/users", produces="application/json;charset=UTF-8")
    public String usersList() throws JSONException {
        ArrayList<String> users =  new ArrayList<String>(){{
            add("freewolf");
            add("tom");
            add("jerry");
        }};

        return this.fillResultString(0, "", users);
    }

}
