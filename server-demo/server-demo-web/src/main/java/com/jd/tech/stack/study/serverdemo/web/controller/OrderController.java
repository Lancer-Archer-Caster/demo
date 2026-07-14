package com.jd.tech.stack.study.serverdemo.web.controller;

import com.jd.tech.stack.study.serverdemo.client.order.api.OrderService;
import com.jd.tech.stack.study.serverdemo.client.order.param.OrderParam;
import com.jd.tech.stack.study.serverdemo.client.order.dto.OrderDTO;
import com.jd.tech.stack.study.serverdemo.web.converter.OrderDTOConverter;
import com.jd.tech.stack.study.serverdemo.infra.config.LocalConfiguration;
import com.jd.tech.stack.study.serverdemo.web.vo.OrderVO;
import com.jd.tech.stack.study.serverdemo.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Description: Web Demo
 *
 * @author DongBoot
 */

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private LocalConfiguration localConfiguration;
    @Autowired
    private OrderService orderService;

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam(name = "name", defaultValue = "demo") String name) {
        return "hello " + name;
    }


    /**
     * 原 RPC 接口的本地兼容入口。
     * <a href="http://localhost:8080/order/getfromrpc?key=demo">get</a>
     * <p>
     *
     * @see <a href="https://joyspace.jd.com/pages/nusEIcCzJrI6FFLoWdJh">JSF使用说明</a>
     * @see <a href="https://taishan.jd.com/jsf/interfaceList">JSF管理端</a>
     */
    @RequestMapping("/getfromrpc")
    @ResponseBody
    public Result<OrderVO> getOrderFromRpc(@RequestParam(name = "id", defaultValue = "demo") String id) {
        OrderDTO orderDTO = orderService.getFromRpc(new OrderParam(id));
        OrderVO orderVO = OrderDTOConverter.convert(orderDTO);
        return new Result<>(orderVO, "get from rpc success");
    }
    /**
     * DongDAL 测试
     * <a href="http://localhost:8080/order/getfromdb?key=demo">get</a>
     * <p>
     *
     * @see <a href="https://mybatis.org/spring-boot-starter/">Mybatis 使用说明</a>
     * @see <a href="https://joyspace.jd.com/pages/om3n1qZjOTSVGzCqBPJZ">DongDAL 使用说明</a>
     */
    @RequestMapping("/getfromdb")
    @ResponseBody
    public Result<OrderVO> getOrderFromDb(@RequestParam(name = "id", defaultValue = "demo") String id) {
        OrderDTO orderDTO = orderService.getFromDb(new OrderParam(id));
        OrderVO orderVO = OrderDTOConverter.convert(orderDTO);
        return new Result<>(orderVO, "get from db success");
    }
    /**
     * <a href="http://localhost:8080/order/contextinfo">get</a>
     *
     * @see <a href="https://joyspace.jd.com/pages/vGFvw0xpAwjtoRwn8Kig">全局上下文 使用说明</a>
     */
    @RequestMapping("/contextinfo")
    @ResponseBody
    public Result<String> contextInfo(HttpServletRequest request) {
        String requestId = request.getHeader("X-Request-Id");
        if (requestId == null || requestId.trim().isEmpty()) {
            requestId = UUID.randomUUID().toString();
        }
        return new Result<>("requestId: " + requestId + ", context info: local");
    }
    /**
     * 本地配置测试。保留旧路径，避免已有前端调用失效。
     *
     * <a href="http://localhost:8080/order/duccget">get</a>
     */
    @RequestMapping("/duccget")
    @ResponseBody
    public Result<String> getDuccConfig() {
        return new Result<>("get from local config success, value is: " + localConfiguration.getMessage());
    }

    /**
    * Description: DongGuardian Demo
    *
    * @author DongBoot
    * @see <a href="https://joyspace.jd.com/pages/Ph29w9cuZRYrd3neN1O5">Description使用说明</a>
    * @see <a href="https://taishan.jd.com/dongguardian/limitIndex">Description管理端</a>
    */

    @RequestMapping("/testCluster")
    @ResponseBody
    public String testCluster() {
         return orderService.testCluster(1L);
    }

    @RequestMapping("/testCluster1")
    @ResponseBody
    public String testCluster1() {
        return orderService.testCluster1();
    }





}
