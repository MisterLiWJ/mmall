package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liwj on 2018/6/5 0005.
 */
public class RedisShardedPool {



    private static ShardedJedisPool pool;//jedis连接池
    private static Integer maxTotal= PropertiesUtil.getPropertyInt("redis.max.total","20");//jedis最大连接数
    private static Integer maxIdel= PropertiesUtil.getPropertyInt("redis.max.idel","10");//最大空闲链接个数
    private static Integer minIdel= PropertiesUtil.getPropertyInt("redis.min.idel","2");//最小空闲链接数量
    private static Boolean testOnBorrw=PropertiesUtil.getPropertyBoolean("redis.test.borrow",true);//在borrw一个jedis实例,是否验证这个jedis实例是否是可用的,如果为true是肯定能用的
    private static Boolean testOnReturn=PropertiesUtil.getPropertyBoolean("redis.test.return",true);//在回收一个jedis的时候是否要进行验证操作,如果true代表回收的时候肯定是一个可用的jedis实例
    private final static Integer outTIme=PropertiesUtil.getPropertyInt("redis.db.outtime","2000");//设置超时时间为两秒
    private static String auth=PropertiesUtil.getProperty("redis.db.passworld","liwj2000");//链接密码

    ///////////////////////////////////////第一台Redis//////////////////////////////////////////////////////
    private static String host1=PropertiesUtil.getProperty("redis1.db.host","127.0.0.1");//链接地址
    private static Integer port1=PropertiesUtil.getPropertyInt("redis1.db.port","6379");//端口号

    ///////////////////////////////////////第二台Redis//////////////////////////////////////////////////////
    private static String host2=PropertiesUtil.getProperty("redis2.db.host","127.0.0.1");//链接地址
    private static Integer port2=PropertiesUtil.getPropertyInt("redis2.db.port","6380");//端口号

    /**
     * 初始化连接池,
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
        JedisShardInfo info1=new JedisShardInfo(host1,port1,outTIme);
        info1.setSoTimeout(outTIme);

        //设置连接密码
        //        info1.setPassword("");
        JedisShardInfo info2=new JedisShardInfo(host2,port2,outTIme);
        info2.setSoTimeout(outTIme);
        List<JedisShardInfo> list=new ArrayList<>(2);
        list.add(info1);
        list.add(info2);
        pool=new ShardedJedisPool(jedisPoolConfig,list, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }
    static {
        poolInit();
    }

    /**
     * 获取链接对象
     * @return
     */
    public static ShardedJedis getJedis(){
        return pool.getResource();
    }

    /**
     * 回收链接
     */
    public static void returnResource(ShardedJedis jedis){
        if(jedis!=null){
            pool.returnResource(jedis);
        }
    }

    public static void blockBorkenResource(ShardedJedis jedis){
        //这不一部源码判断了是否为空
        pool.returnBrokenResource(jedis);
    }

    public static void main(String[] args) {
        ShardedJedis jedis=pool.getResource();
        for(int i=0;i<10;i++){
            jedis.set("key:"+i,"value:"+i);
        }
        pool.destroy();//销毁连接池所有链接
    }


}
