package com.guan.provider.service.impl;

import com.guan.bean.Order;
import com.guan.order.OrderService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mr.Guan
 * @since 2019/11/18
 */
@Service
@Component
public class OrderServiceImpl implements OrderService {
    @Override
    public List<Order> getOrderList() {
        Order order = new Order();
        order.setId(1);
        order.setOrderNum("14s4df4s5d4fs5d4f5s45");
        order.setPrice(new BigDecimal(11));
        order.setDescription("描述信息");

        Order order2 = new Order();
        order2.setId(2);
        order2.setOrderNum("2dsf4s54ds5d4sd78s7d8f");
        order2.setPrice(new BigDecimal(22));
        order2.setDescription("这个12块");

        return Arrays.asList(order,order2);
    }
}
