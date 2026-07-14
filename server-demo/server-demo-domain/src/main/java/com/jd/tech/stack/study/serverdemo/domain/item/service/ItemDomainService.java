package com.jd.tech.stack.study.serverdemo.domain.item.service;

import com.jd.tech.stack.study.serverdemo.domain.item.converter.ItemDOConverter;
import com.jd.tech.stack.study.serverdemo.domain.item.bo.ItemBO;
import com.jd.tech.stack.study.serverdemo.domain.order.bo.OrderBO;
import com.jd.tech.stack.study.serverdemo.infra.item.dataobject.ItemDO;
import com.jd.tech.stack.study.serverdemo.infra.item.gateway.ItemGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemDomainService {

    @Autowired
    private ItemGateway itemGateway;

    public ItemBO doSomething(OrderBO orderBO, ItemBO itemBO) {
        ItemDO itemDO = itemGateway.getById("demo");
        return ItemDOConverter.convert(itemDO);
    }
}
