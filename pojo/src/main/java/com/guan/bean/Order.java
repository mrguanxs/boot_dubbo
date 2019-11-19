package com.guan.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Mr.Guan
 * @since 2019/11/18
 */
@Data
public class Order implements Serializable{

    private Integer id;

    private String orderNum;

    private BigDecimal price;

    private String description;
}
