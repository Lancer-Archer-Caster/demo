package com.jd.tech.stack.study.serverdemo.infra;

import com.jd.tech.stack.study.serverdemo.infra.item.dataobject.ItemDO;
import com.jd.tech.stack.study.serverdemo.infra.item.gateway.ItemGateway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ItemGatewayTest {

    @Autowired
    private ItemGateway itemGateway;

    @Test
    public void TestItemGateway() {
    ItemDO itemDO = itemGateway.getById("demo");
    assertEquals("dongboot", itemDO.getName());
    }

}
