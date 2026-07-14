package com.jd.tech.stack.study.serverdemo.infra.order.converter;

import com.jd.tech.stack.study.serverdemo.client.order.dto.OrderDTO;
import com.jd.tech.stack.study.serverdemo.infra.order.dataobject.OrderDO;

public class OrderDTOConverter {
    public static OrderDO convert(OrderDTO orderDTO) {
        OrderDO orderDO = new OrderDO();
        orderDO.setId(orderDTO.getId());
        orderDO.setValue(orderDTO.getValue());
        return orderDO;
    }
}
