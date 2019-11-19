package com.guan.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Mr.Guan
 * @since 2019/11/18
 */
@Data
public class User  implements Serializable {

    private String name;

    private Integer age;

    private Integer sex;

    private String address;
}
