package com.mmall.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


//包含了getter,setter
@Data
//无参数构造器
@NoArgsConstructor
//全参数构造器
@AllArgsConstructor
public class Cart {
    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer quantity;
    private Integer checked;
    private Date createTime;
    private Date updateTime;
}