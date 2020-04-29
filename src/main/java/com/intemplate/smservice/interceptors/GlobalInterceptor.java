package com.intemplate.smservice.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

@Component
public class GlobalInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = LoggerFactory.getLogger(GlobalInterceptor.class);

    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]); // Optional 强制赋默认值,cookies一定不为 null
        String cookie = Arrays.stream(cookies).filter(item ->
                "X-Auth-Token".equals(item.getName())
        ).findFirst().map(Cookie::getValue).orElse(null);
        return cookie;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = getTokenFromCookie(request);
        logger.info("TOKEN:" + token);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
}
