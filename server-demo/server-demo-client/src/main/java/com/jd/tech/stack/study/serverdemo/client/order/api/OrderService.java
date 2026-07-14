package com.jd.tech.stack.study.serverdemo.client.order.api;

import com.jd.tech.stack.study.serverdemo.client.order.param.OrderParam;
import com.jd.tech.stack.study.serverdemo.client.order.dto.OrderDTO;

/**
 * Description: JSF Demo
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public interface OrderService {

    OrderDTO getFromRpc(OrderParam param);

    OrderDTO getFromDb(OrderParam param);




    String testCluster(long s);

    String testCluster1();



}
