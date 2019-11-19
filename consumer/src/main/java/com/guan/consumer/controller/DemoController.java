package com.guan.consumer.controller;



import com.guan.bean.Order;
import com.guan.order.OrderService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * @author guanxinshuai E-mail:
 * @version 创建时间：2019/11/19 10:03 AM
 * Demo:
 */
@Controller
public class DemoController {

    @Reference
//    @Autowired
    private OrderService orderService;

    @ResponseBody
    @RequestMapping("/hello")
    public List<Order> getOrderList(){
        return orderService.getOrderList();
    }
}