package com.mmall.util;

import com.mmall.common.RedisPool;
import com.mmall.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

/**
 * Created by liwj on 2018-05-25.
 * 分布式和集群,物理形态和工作关系分析
 *
 */
@Slf4j
public class RedisShardedPoolUtil {

    public static Long expire(String key,int exTime){
        ShardedJedis jedis=null;
        long result=0;
        try{
            jedis= RedisShardedPool.getJedis();
            result=jedis.expire(key, exTime);
        }catch (Exception e){
            log.error("expire key:{}error:{}",key,e);
            RedisShardedPool.blockBorkenResource(jedis);
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static Long del(String key){
        ShardedJedis jedis=null;
        long result=0;
        try{
            jedis=RedisShardedPool.getJedis();
            result=jedis.del(key);
        }catch (Exception e){
            log.error("del key:{}error:{}",key,e);
            RedisShardedPool.blockBorkenResource(jedis);
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static String setEx(String key,String val,int exTime){
        ShardedJedis jedis=null;
        String result="";
        try{
            jedis=RedisShardedPool.getJedis();
            result=jedis.setex(key, exTime,val);
        }catch (Exception e){
            log.error("setEx key:{}value:{}error:{}",key,val,e);
            RedisShardedPool.blockBorkenResource(jedis);
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static String set(String key,String val){
        ShardedJedis jedis=null;
        String result="";
        try{
            jedis=RedisShardedPool.getJedis();
            result=jedis.set(key, val);
        }catch (Exception e){
            log.error("set key:{}value:{}error:{}",key,val,e);
            RedisShardedPool.blockBorkenResource(jedis);
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static String get(String key){
        ShardedJedis jedis=null;
        String result="";
        try{
            jedis=RedisShardedPool.getJedis();
            result=jedis.get(key);
        }catch (Exception e){
            log.error("get key:{},error:{}",key,e);
            RedisShardedPool.blockBorkenResource(jedis);
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }


}
