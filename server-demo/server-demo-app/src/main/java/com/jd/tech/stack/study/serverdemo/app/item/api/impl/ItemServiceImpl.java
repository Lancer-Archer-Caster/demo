package com.jd.tech.stack.study.serverdemo.app.item.api.impl;

import com.jd.tech.stack.study.serverdemo.app.item.converter.ItemBOConverter;
import com.jd.tech.stack.study.serverdemo.client.item.api.ItemService;
import com.jd.tech.stack.study.serverdemo.client.item.dto.ItemDTO;
import com.jd.tech.stack.study.serverdemo.client.item.param.ItemParam;
import com.jd.tech.stack.study.serverdemo.domain.item.bo.ItemBO;
import com.jd.tech.stack.study.serverdemo.domain.item.service.ItemDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

    private static final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Autowired
    private ItemDomainService itemDomainService;

    @Override
    public ItemDTO sayHello(ItemParam param) {
        log.info("sayHello {}", param.getReq());
        ItemBO itemBO = itemDomainService.doSomething(null, null);
        return ItemBOConverter.convert(itemBO);
    }
}
