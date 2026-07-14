package com.jd.tech.stack.study.serverdemo.infra.order.gateway.impl;
import com.jd.tech.stack.study.serverdemo.infra.order.dataobject.OrderDO;
import com.jd.tech.stack.study.serverdemo.infra.order.gateway.OrderGateway;
import com.jd.tech.stack.study.serverdemo.infra.order.mapper.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderGatewayImpl implements OrderGateway {

    private static final Logger log = LoggerFactory.getLogger(OrderGatewayImpl.class);

    @Autowired
    private OrderMapper orderMapper;
    @Override
    public OrderDO getByIdFromDb(String orderId) {
        return orderMapper.getById(orderId);
    }

    @Override
    public OrderDO getByIdFromRpc(String orderId) {
        // 兼容原有方法名和接口路径，实际读取本地 H2，不再发起 JSF/RPC 调用。
        log.debug("Resolve former RPC request locally, orderId={}", orderId);
        return orderMapper.getById(orderId);
    }
}
