package com.jd.tech.stack.study.serverdemo.domain;

import com.jd.tech.stack.study.serverdemo.domain.item.bo.ItemBO;
import com.jd.tech.stack.study.serverdemo.domain.item.service.ItemDomainService;
import com.jd.tech.stack.study.serverdemo.domain.order.bo.OrderBO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ItemDomainServiceTest {

    @Autowired
    ItemDomainService itemDomainService;

    @Test
    public void TestItemDomainService() {
        OrderBO orderBO = new OrderBO();
        orderBO.setId("demo");
        ItemBO itemBO = new ItemBO("demo");
        ItemBO result = itemDomainService.doSomething(orderBO, itemBO);
        assertEquals("dongboot", result.getName());
    }

}
