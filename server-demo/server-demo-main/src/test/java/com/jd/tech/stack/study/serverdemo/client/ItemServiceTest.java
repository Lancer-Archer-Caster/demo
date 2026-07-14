package com.jd.tech.stack.study.serverdemo.client;

import com.jd.tech.stack.study.serverdemo.client.item.api.ItemService;
import com.jd.tech.stack.study.serverdemo.client.item.dto.ItemDTO;
import com.jd.tech.stack.study.serverdemo.client.item.param.ItemParam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Test
    public void testFromRpc() {
        ItemParam param = new ItemParam();
        param.setReq("demo");
        ItemDTO itemDTO = itemService.sayHello(param);
        assertEquals("dongboot", itemDTO.getResp());
    }

}
