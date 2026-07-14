package com.jd.tech.stack.study.serverdemo.infra.item.gateway;

import com.jd.tech.stack.study.serverdemo.infra.item.dataobject.ItemDO;

public interface ItemGateway {
    ItemDO getById(String itemId);
}
