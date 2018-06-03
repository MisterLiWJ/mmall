package com.mmall.pojo;

import lombok.*;

import java.util.Date;

//如果这种get,set别的类使用不了,检查是否安装lombok插件
//包含了getter,setter
@Data
//无参数构造器
@NoArgsConstructor
//全参数构造器
@AllArgsConstructor
//实现eqals和hashcode比较,指定作用于id上面,内部实现使比较了所有的属性的,对象比较内部使用instantOf比较的
@EqualsAndHashCode(of="id")
//重写toString方法,默认重写所有的,exclude排除指定的属性,多个使用数组
@ToString(exclude = "updateTime")
public class Category {
    private Integer id;
    private Integer parentId;
    private String name;
    private Boolean status;
    private Integer sortOrder;
    private Date createTime;
    private Date updateTime;
}