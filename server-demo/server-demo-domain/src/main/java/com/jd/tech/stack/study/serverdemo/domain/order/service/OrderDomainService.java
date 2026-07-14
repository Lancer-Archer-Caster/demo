package com.jd.tech.stack.study.serverdemo.domain.order.service;

import com.jd.tech.stack.study.serverdemo.domain.order.bo.OrderBO;
import com.jd.tech.stack.study.serverdemo.domain.order.converter.OrderDOConverter;
import com.jd.tech.stack.study.serverdemo.infra.order.dataobject.OrderDO;
import com.jd.tech.stack.study.serverdemo.infra.order.gateway.OrderGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDomainService {

    private static final Logger log = LoggerFactory.getLogger(OrderDomainService.class);

    @Autowired
    private OrderGateway orderGateway;

    public OrderBO getFromDb(String orderId) {
        OrderDO orderDO = orderGateway.getByIdFromDb(orderId);
        return OrderDOConverter.convert(orderDO);
    }


    public OrderBO getFromRpc(String orderId) {
        OrderDO orderDO = orderGateway.getByIdFromRpc(orderId);
        return OrderDOConverter.convert(orderDO);
    }


}
