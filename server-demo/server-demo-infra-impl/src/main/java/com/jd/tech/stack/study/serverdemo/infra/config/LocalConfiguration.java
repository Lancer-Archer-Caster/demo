package com.jd.tech.stack.study.serverdemo.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 从本地 application.properties 或环境变量读取演示配置。
 */
@Component
public class LocalConfiguration {

    @Value("${demo.message:JoyCue local demo}")
    private String message;

    public String getMessage() {
        return message;
    }
}
