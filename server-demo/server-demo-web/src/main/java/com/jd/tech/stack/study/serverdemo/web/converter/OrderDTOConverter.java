package com.jd.tech.stack.study.serverdemo.web.converter;

import com.jd.tech.stack.study.serverdemo.client.order.dto.OrderDTO;
import com.jd.tech.stack.study.serverdemo.web.vo.OrderVO;

public class OrderDTOConverter {
    public static OrderVO convert(OrderDTO orderDTO) {
        // convert DTO to VO
        OrderVO orderVO = new OrderVO();
        orderVO.setId(orderDTO.getId());
        orderVO.setValue(orderDTO.getValue());
        return orderVO;
    }
}
