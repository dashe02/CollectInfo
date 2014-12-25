package org.collectinfo.redis;

import org.collectinfo.mina.config.ConfigParser;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created with IntelliJ IDEA.
 * User: hadoop
 * Date: 14-12-25
 * Time: 下午8:21
 * To change this template use File | Settings | File Templates.
 */
public class RedisConnector {
    private static int expire;
    private static JedisPool pool;
    private static ConfigParser configParser=new ConfigParser();
    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxActive(Integer.valueOf(configParser.getPros().get("redis.pool.maxActive").toString()));
        config.setMaxIdle(Integer.valueOf(configParser.getPros().get("redis.pool.maxIdle").toString()));
        config.setMaxWait(Long.valueOf(configParser.getPros().get("redis.pool.maxWait").toString()));
        config.setTestOnBorrow(Boolean.valueOf(configParser.getPros().get("redis.pool.testOnBorrow").toString()));
        config.setTestOnReturn(Boolean.valueOf(configParser.getPros().get("redis.pool.testOnReturn").toString()));
        pool = new JedisPool(config, configParser.getPros().get("redis.ip").toString(), Integer.valueOf(configParser.getPros().get("redis.port").toString()));
        expire = Integer.valueOf(configParser.getPros().get("redis.expire").toString());
    }
    public RedisConnector() {}
    public int getExpire() {
        return expire;
    }

    public boolean set(String key,String value){
        Jedis jedis = pool.getResource();
        try{
            String result = jedis.set(key,value);
            if (result.equals("OK")){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            pool.returnResource(jedis);
        }
        return false;
    }

    public boolean set(String key,int expire,String value){
        Jedis jedis = pool.getResource();
        try{
            String result = jedis.setex(key,expire,value);
            if (result.equals("OK")){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            pool.returnResource(jedis);
        }
        return false;
    }

    public String get(String key){
        Jedis jedis = pool.getResource();
        String result = null;
        try{
            result = jedis.get(key);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            pool.returnResource(jedis);
        }
        return result;
    }
    public Long remove(String key){
        Jedis jedis = pool.getResource();
        Long result = null;
        try{
            result = jedis.del(key);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            pool.returnResource(jedis);
        }
        return result;
    }
}
