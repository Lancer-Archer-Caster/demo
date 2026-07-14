package com.jd.tech.stack.study.serverdemo.domain.order.converter;

import com.jd.tech.stack.study.serverdemo.domain.order.bo.OrderBO;
import com.jd.tech.stack.study.serverdemo.infra.order.dataobject.OrderDO;

public class OrderDOConverter {

    public static OrderBO convert(OrderDO orderDO) {
        // convert DO to BO
        OrderBO orderBO = new OrderBO();
        if (orderDO != null) {
            orderBO.setId(orderDO.getId());
            orderBO.setValue(orderDO.getValue());
        }
        return orderBO;
    }

    public static OrderDO convert(OrderBO orderBO) {
        // convert BO to DO
        OrderDO orderDO = new OrderDO();
        if (orderBO != null) {
            orderDO.setId(orderBO.getId());
            orderDO.setValue(orderBO.getValue());
        }
        return orderDO;
    }
}
