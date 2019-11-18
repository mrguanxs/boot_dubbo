package com.guan.order;


import com.guan.bean.Order;

import java.util.List;

/**
 * @author Mr.Guan
 * @since 2019/11/18
 */
public interface OrderService {

        List<Order> getOrderList();
}
