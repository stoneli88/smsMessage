package com.intemplate.smservice.models;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

public class ImageCode {
    private String code;

    // 过期时间
    private LocalDateTime expireTime;

    // 图片
    private BufferedImage image;

    // 验证是否过期
    public boolean isExpire () {
        return LocalDateTime.now().isAfter(expireTime);
    }

    public ImageCode(BufferedImage image, String code,  int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
        this.image = image;
    }

    // expireIn 过多久图形验证码 过期
    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime){
        this.code = code;
        this.expireTime = expireTime;
        this.image = image;
    }

    public ImageCode(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
