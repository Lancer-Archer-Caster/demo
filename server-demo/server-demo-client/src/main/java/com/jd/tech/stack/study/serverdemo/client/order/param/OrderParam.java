package com.jd.tech.stack.study.serverdemo.client.order.param;
/**
 * Description: ReqDTO
 * @author DongBoot
 * @date 2024-11-29
 */
public class OrderParam {

    private String id;

    public OrderParam(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

