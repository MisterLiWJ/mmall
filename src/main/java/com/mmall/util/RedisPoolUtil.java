package com.mmall.util;

import com.mmall.common.RedisPool;
import com.mmall.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

/**
 * Created by liwj on 2018-05-25.
 */
@Slf4j
public class RedisPoolUtil {

    public static Long expire(String key,int exTime){
        Jedis jedis=null;
        long result=0;
        try{
            jedis= RedisPool.getJedis();
            result=jedis.expire(key, exTime);
        }catch (Exception e){
            log.error("expire key:{}error:{}",key,e);
            RedisPool.blockBorkenResource(jedis);
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static Long del(String key){
        Jedis jedis=null;
        long result=0;
        try{
            jedis=RedisPool.getJedis();
            result=jedis.del(key);
        }catch (Exception e){
            log.error("del key:{}error:{}",key,e);
            RedisPool.blockBorkenResource(jedis);
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static String setEx(String key,String val,int exTime){
        Jedis jedis=null;
        String result="";
        try{
            jedis=RedisPool.getJedis();
            result=jedis.setex(key, exTime,val);
        }catch (Exception e){
            log.error("setEx key:{}value:{}error:{}",key,val,e);
            RedisPool.blockBorkenResource(jedis);
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static String set(String key,String val){
        Jedis jedis=null;
        String result="";
        try{
            jedis=RedisPool.getJedis();
            result=jedis.set(key, val);
        }catch (Exception e){
            log.error("set key:{}value:{}error:{}",key,val,e);
            RedisPool.blockBorkenResource(jedis);
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static String get(String key){
        Jedis jedis=null;
        String result="";
        try{
            jedis=RedisPool.getJedis();
            result=jedis.get(key);
        }catch (Exception e){
            log.error("get key:{},error:{}",key,e);
            RedisPool.blockBorkenResource(jedis);
        }
        RedisPool.returnResource(jedis);
        return result;
    }


}
