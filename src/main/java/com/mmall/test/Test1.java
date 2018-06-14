package com.mmall.test;

import com.mmall.util.JsonUtil;
import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * Created by Administrator on 2018/6/3 0003.
 */
public class Test1 {
    public static void main(String args[]){
        Jedis jedis=new Jedis("47.97.112.194",6379);
        jedis.auth("liwj2000");
        Map<String,String> map=jedis.hgetAll("*");
        System.out.println(JsonUtil.obj2String(map));

    }
}
