package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by Administrator on 2018-05-18.
 */
public class RedisPool {

    private static JedisPool pool;//jedis连接池
    private static Integer maxTotal= PropertiesUtil.getPropertyInt("redis.max.total","20");//jedis最大连接数
    private static Integer maxIdel= PropertiesUtil.getPropertyInt("redis.max.idel","10");//最大空闲链接个数
    private static Integer minIdel= PropertiesUtil.getPropertyInt("redis.min.idel","2");//最小空闲链接数量
    private static Boolean testOnBorrw=PropertiesUtil.getPropertyBoolean("redis.test.borrow",true);//在borrw一个jedis实例,是否验证这个jedis实例是否是可用的,如果为true是肯定能用的
    private static Boolean testOnReturn=PropertiesUtil.getPropertyBoolean("redis.test.return",true);//在回收一个jedis的时候是否要进行验证操作,如果true代表回收的时候肯定是一个可用的jedis实例
    private final static Integer outTIme=PropertiesUtil.getPropertyInt("redis.db.outtime","2000");//设置超时时间为两秒
    private static String host=PropertiesUtil.getProperty("redis.db.host","47.97.112.194");//链接地址
    private static Integer port=PropertiesUtil.getPropertyInt("redis.db.port","6379");//端口号
    private static String auth=PropertiesUtil.getProperty("redis.db.passworld","liwj2000");//链接密码

    /**
     * 初始化连接池
     */
    public static void poolInit(){
        JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdel);
        jedisPoolConfig.setMinIdle(minIdel);
        jedisPoolConfig.setTestOnBorrow(testOnBorrw);
        jedisPoolConfig.setTestOnReturn(testOnReturn);
        //连接池耗尽的时候是否阻塞,true会阻塞,直到链接超时,false会抛出异常,默认为true
        jedisPoolConfig.setBlockWhenExhausted(true);
        pool=new JedisPool(jedisPoolConfig,host,port,outTIme,auth);
    }
    static {
        poolInit();
    }

    /**
     * 获取链接对象
     * @return
     */
    public static Jedis getJedis(){
        return pool.getResource();
    }

    /**
     * 回收链接
     */
    public static void returnResource(Jedis jedis){
        if(jedis!=null){
            pool.returnResource(jedis);
        }
    }

    public static void blockBorkenResource(Jedis jedis){
        //这不一部源码判断了是否为空
            pool.returnBrokenResource(jedis);
    }

    public static void main(String[] args) {
        Jedis jedis=pool.getResource();
        jedis.set("ceshi","测试");
        System.out.println(jedis.get("ceshi"));
        pool.destroy();//销毁连接池所有链接
    }


}
