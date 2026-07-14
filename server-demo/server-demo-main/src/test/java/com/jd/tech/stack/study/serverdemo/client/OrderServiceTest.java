package com.jd.tech.stack.study.serverdemo.client;

import com.jd.tech.stack.study.serverdemo.client.order.api.OrderService;
import com.jd.tech.stack.study.serverdemo.client.order.dto.OrderDTO;
import com.jd.tech.stack.study.serverdemo.client.order.param.OrderParam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void testFromDb() {
        OrderDTO orderDTO = orderService.getFromDb(new OrderParam("demo"));
        assertEquals("dongboot", orderDTO.getValue());
    }




    @Test
    public void testCluster() {
        String result = orderService.testCluster(100);
        assertEquals("resource is testCluster, param is:100",result);
    }

    @Test
    public void testCluster1() {
        String result  = orderService.testCluster1();
        assertEquals("resource is testCluster1",result);
    }
}
