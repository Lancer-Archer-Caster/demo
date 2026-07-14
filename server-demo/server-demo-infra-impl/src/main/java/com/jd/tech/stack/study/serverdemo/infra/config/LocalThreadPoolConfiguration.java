package com.jd.tech.stack.study.serverdemo.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class LocalThreadPoolConfiguration {

    @Bean(destroyMethod = "shutdown")
    public ThreadPoolExecutor threadPoolExecutor() {
        return (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
    }

    @Bean(destroyMethod = "shutdown")
    public ScheduledThreadPoolExecutor scheduledThreadPoolExecutor() {
        return new ScheduledThreadPoolExecutor(2);
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("joycue-local-");
        return executor;
    }
}
