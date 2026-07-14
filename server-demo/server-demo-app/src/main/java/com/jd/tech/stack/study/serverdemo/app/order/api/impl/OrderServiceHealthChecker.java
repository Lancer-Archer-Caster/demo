package com.jd.tech.stack.study.serverdemo.app.order.api.impl;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Description: 健康检查HealthChecker Demo, 本身是对外提供服务的业务模块, 实现了HealthChecker接口，健康检查中会根据本业务模块的状态判断应用是否健康
 *
 * @author DongBoot
 * @see <a href="https://joyspace.jd.com/pages/1rdSfDgxS8k29Btcc9cx">健康检查 使用说明</a>
 */
@Service
public class OrderServiceHealthChecker implements HealthIndicator {

    /**
     * 健康检查项名称常量
     */
    public static final String BIZ_ONE_SERVICE = "DEMO-HEALTH-CHECKER";

    /**
     * 初始化和预热操作
     */
    @PostConstruct
    public void init() {
    }

    /**
     * 业务状态
     *
     * @return 返回业务状态
     */
    public String status() {
        return "ok";
    }

    /**
     * 健康检查的结果
     *
     * @return 返回健康检查结果
     */
    @Override
    public Health health() {
        return Health.up().build();
    }

    /**
     * 健康检查项名称
     *
     * @return 健康检查项名称
     */
    public String getComponentName() {
        return BIZ_ONE_SERVICE;
    }

}
