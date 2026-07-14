package com.jd.tech.stack.study.serverdemo.infra.item.gateway.impl;

import com.jd.tech.stack.study.serverdemo.infra.item.dataobject.ItemDO;
import com.jd.tech.stack.study.serverdemo.infra.item.gateway.ItemGateway;
import com.jd.tech.stack.study.serverdemo.infra.item.mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemGatewayImpl implements ItemGateway {

    @Autowired
    private ItemMapper itemMapper;

    @Override
    public ItemDO getById(String itemId) {
        ItemDO itemDO = itemMapper.getById(itemId);
        return itemDO;
    }
}
