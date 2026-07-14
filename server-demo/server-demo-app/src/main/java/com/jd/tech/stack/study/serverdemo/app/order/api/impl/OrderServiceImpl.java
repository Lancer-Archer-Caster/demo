package com.jd.tech.stack.study.serverdemo.app.order.api.impl;

import com.jd.tech.stack.study.serverdemo.app.order.converter.OrderBOConverter;
import com.jd.tech.stack.study.serverdemo.client.order.api.OrderService;
import com.jd.tech.stack.study.serverdemo.client.order.dto.OrderDTO;
import com.jd.tech.stack.study.serverdemo.client.order.param.OrderParam;
import com.jd.tech.stack.study.serverdemo.domain.order.bo.OrderBO;
import com.jd.tech.stack.study.serverdemo.domain.order.service.OrderDomainService;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDomainService orderDomainService;

    @Override
    public OrderDTO getFromRpc(OrderParam param) {
        log.info("orderservice getFromRpc {}", param.getId());
        OrderBO orderBO = orderDomainService.getFromRpc(param.getId());
        return OrderBOConverter.convert(orderBO);
    }


    @Override
    public OrderDTO getFromDb(OrderParam param) {
        log.info("orderservice getFromDb {}", param.getId());
        OrderBO orderBO = orderDomainService.getFromDb(param.getId());
        return OrderBOConverter.convert(orderBO);
    }




    @Override
    public String testCluster(long s) {
        return String.format("resource is testCluster, param is:%d", s);
    }

    public String helloFallback(long s, Throwable ex) {
        // Do some log here.
        System.out.println("Go to helloFallback");
        ex.printStackTrace();
        return "testCluster is be limited, error occurred at " + s;
    }


    @Override
    public String testCluster1() {
        return "resource is testCluster1";
    }



}
