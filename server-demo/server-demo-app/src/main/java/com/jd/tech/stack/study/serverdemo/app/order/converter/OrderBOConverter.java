package com.jd.tech.stack.study.serverdemo.app.order.converter;

import com.jd.tech.stack.study.serverdemo.client.order.dto.OrderDTO;
import com.jd.tech.stack.study.serverdemo.domain.order.bo.OrderBO;

public class OrderBOConverter {

    public static OrderDTO convert(OrderBO orderBO) {
        // convert BO to DTO
        OrderDTO orderDTO = new OrderDTO();
        if (orderBO != null) {
            orderDTO.setId(orderBO.getId());
            orderDTO.setValue(orderBO.getValue());
        }
        return orderDTO;
    }
}
