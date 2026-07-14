package com.jd.tech.stack.study.serverdemo.infra.order.gateway;

import com.jd.tech.stack.study.serverdemo.infra.order.dataobject.OrderDO;

public interface OrderGateway {

    OrderDO getByIdFromDb(String orderId);
    OrderDO getByIdFromRpc(String orderId);
}
