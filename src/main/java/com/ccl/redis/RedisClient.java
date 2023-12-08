package com.ccl.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 　 * <p>Title: RedisClient.java</p>
 * 　 * <p>Description: 使用原则上，每一个key都应该按照分类建立对应的文件目录，便于管理；
 * 尽量避免直接往redis中设置key,value键值对</p>
 * 　 * <p>Copyright: Copyright (c) 2020</p>
 * 　 * <p>Company: www.furentech.com/</p>
 * 　 * @author yangy
 * 　 * @date 2020年5月28日 下午4:04:48
 * 　 * @version 1.0
 */
@Service
public class RedisClient {
    //RedisAutoConfiguration d;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate<String, Serializable> redisTemplate;

    public StringRedisTemplate getTemplate() {
        return stringRedisTemplate;
    }

    public RedisTemplate<String, Serializable> getRedisObjTemplate() {
        return redisTemplate;
    }

    /**
     * 返回有序的序列化redis
     *
     * @return
     */
    public ZSetOperations<String, Serializable> getRedisObjZset() {
        return redisTemplate.opsForZSet();
    }

    public ZSetOperations<String, String> getRedisStringZset() {
        return stringRedisTemplate.opsForZSet();
    }

    public ZSetOperations<String, String> getRedisZset() {
        return stringRedisTemplate.opsForZSet();
    }

    /**
     * 添加基础key，value数据
     *
     * @param key   主键
     * @param value 值
     */
    public void addStringValue(String key, String value) {
        if (StringUtils.isBlank(key)) {
            return;
        }
        getTemplate().opsForValue().set(key, value);
    }

    /**
     * @Author liuc
     * @Description 普通缓存放入并设置时间
     * @Date 2021/8/27 15:36
     * @Param [key, value, time]
     * @return void
     **/
    public void addStringExpire(String key, String value, long time,TimeUnit unit) {
        if (time > 0) {
            redisTemplate.opsForValue().set(key, value);
            setExpireTime(key,time,unit);
        } else {
            addStringValue(key, value);
        }
    }

    /**
     * 删除基础Stirng key
     *
     * @param folder 文件夹（可不填）
     * @param key    键值
     */
    public void deleteStringKey(String folder, String key) {
        getTemplate().delete(createFolderKey(folder, key));
    }

    /**
     * 删除map集合中某个key
     *
     * @param folder map集合文件夹名称
     * @param key
     */
    public void deleteMapKey(String folder, String key) {
        getTemplate().opsForHash().delete(folder, key);
    }

    /**
     * 根据key获取基础数据
     *
     * @param key
     * @return
     */
    public String getValueByKey(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return getTemplate().opsForValue().get(key);
    }

    /**
     * 添加map集合数据
     *
     * @param folder
     * @param key
     * @param m
     */
    public void addMap(String folder, String key, Map<String, Object> m) {
        if (StringUtils.isBlank(key) || m.isEmpty()) {
            return;
        }
        getTemplate().opsForHash().putAll(createFolderKey(folder, key), m);
    }
    
    /**
      * @Description: 添加map集合，并设置过期时间
      * @param folder
      * @param key
      * @param m
      * @param timeout 过期时间
      * @param unit  时间单位
      * @return: void
      * @author: yangy
      * @date: 2021年11月16日 上午10:27:45
      * @version: v1.0
     */
    public void addMapCollect(String folder, String key, Map<String, String> m,long timeout, TimeUnit unit) 
    {
    	if (StringUtils.isBlank(key) || m.isEmpty()) {
            return;
        }
        getTemplate().opsForHash().putAll(createFolderKey(folder, key), m);
        if(!Objects.isNull(timeout)) 
        {
        	this.setExpireTime(createFolderKey(folder, key), timeout, unit);
        }
    }

    /**
     * @param folder map集合目录
     * @param key    记录key
     * @param value
     * @Description: 给map集合单独添加一条记录
     * @return: void
     * @author: yangy
     * @date: 2020年6月19日 下午4:15:33
     * @version: v1.0
     */
    public void addValueToMap(String folder, String key, Object value) {
        if (StringUtils.isBlank(key) || null==value|| StringUtils.isBlank(folder)) {
            return;
        }
        getTemplate().opsForHash().put(folder, key, value);
    }

    /**
     * @param folder map集合目录
     * @param key    记录key
     * @Description: 获取map集合中的某个key值
     * @return: void
     * @author: yangy
     * @date: 2020年6月19日 下午4:20:08
     * @version: v1.0
     */
    public Object getValueFromMap(String folder, String key) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(folder)) {
            return null;
        }
        return getTemplate().opsForHash().get(folder, key);
    }

    /**
     * 获取map集合
     *
     * @param folder
     * @param key
     * @return
     */
    public Map<Object, Object> getMapData(String folder, String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return getTemplate().opsForHash().entries(createFolderKey(folder, key));
    }

    /**
     * 获取redis集合
     *
     * @param folder 文件夹名称
     * @param key
     * @param begin  起始分值
     * @param end    最后分值
     * @return
     */
    public Set<String> getSetData(String folder, String key, Integer begin, Integer end) {
        if (StringUtils.isBlank(key) || Objects.isNull(begin) || Objects.isNull(end)) {
            return null;
        }
        if (StringUtils.isNotBlank(folder)) {
            key = createFolderKey(folder, key);
        }
        return getRedisZset().reverseRange(key, begin, end);
    }

    /**
     * 在某个redis目录下添加key,value数据
     *
     * @param folder 目录
     * @param key    主键
     * @param value  值
     */
    public void addStringValueToFolder(String folder, String key, String value) {
        if (StringUtils.isBlank(folder) || StringUtils.isBlank(key)) {
            return;
        }
        getTemplate().opsForValue().set(createFolderKey(folder, key), value);
    }
    
    /**
      * @Description: 新建带集合KEY和失效时间
      * @param folder 目录
      * @param key  主键
      * @param value 值
      * @param timeout 失效时间
      * @param unit  失效时间单位
      * @return: void
      * @author: yangy
      * @date: 2021年8月30日 上午11:26:02
      * @version: v1.0
     */
    public void addStringValueToFolderExpire(String folder, String key, String value,long timeout, TimeUnit unit) {
        if (StringUtils.isBlank(folder) || StringUtils.isBlank(key)) {
            return;
        }
        getTemplate().opsForValue().set(createFolderKey(folder, key), value);
        setExpireTime(createFolderKey(folder, key),timeout,unit);
    }
    
    public String getStringValueFromFolder(String folder, String key) {
        if (StringUtils.isBlank(folder) || StringUtils.isBlank(key)) {
            return null;
        }
        return getTemplate().opsForValue().get(createFolderKey(folder,key));
    }

    /**
     * 获取某个redis集合的size
     *
     * @param folder
     * @param key
     * @return
     */
    public int getSetSize(String folder, String key) {
        if (StringUtils.isBlank(key)) {
            return 0;
        }
        String folderkey = createFolderKey(folder, key);
        return getRedisStringZset().size(folderkey).intValue();
    }

    /**
     * 添加set集合
     *
     * @param folder 目录
     * @param key    主键
     * @param value  值
     * @param score  排序分值
     */
    public void addStringSet(String folder, String key, String value, double score) {
        if (StringUtils.isBlank(folder) || StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            return;
        }
        String folderkey = createFolderKey(folder, key);
        int size = getRedisStringZset().size(folderkey).intValue();
        score = Double.isNaN(score) ? size + 1 : score;
        getRedisStringZset().add(folderkey, value, score);
    }

    /**
     * 添加set集合,默认排序分值score
     *
     * @param folder 目录
     * @param key    主键
     * @param value  值
     */
    public void addStringSet(String folder, String key, String value) {
        if (StringUtils.isBlank(folder) || StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            return;
        }
        String folderkey = createFolderKey(folder, key);
        int size = getRedisStringZset().size(folderkey).intValue();
        getRedisStringZset().add(folderkey, value, size + 1);
    }

    /**
     * 设置key的redis失效时间，redis会定时从内存清除设置了失效时间的key
     *
     * @param key
     * @param timeout
     * @param unit
     */
    public void setExpireTime(String key, long timeout, TimeUnit unit) {
        //getTemplate().expire("testkey",5*60,TimeUnit.SECONDS); 例：让testkey在5分钟后失效
        getTemplate().expire(key, timeout, unit);
    }

    private String createFolderKey(String folder, String key) {
        StringBuilder setKey = new StringBuilder();
        if (StringUtils.isNotBlank(folder)) {
            setKey.append(folder)
                    .append(":");
        }
        setKey.append(key);
        return setKey.toString();
    }


    /**
     * @Author liuc
     * @Description 根据key或者失效时间
     * 从redis中获取key对应的过期时间;
     * 如果该值有过期时间，就返回相应的过期时间;
     * 如果该值没有设置过期时间，就返回-1;
     * 如果没有该值，就返回-2;
     * @Date 2021/11/8 14:01
     * @Param [key]
     * @return java.lang.Long
     **/
    public Long getExpireTimeByKey(String key) {
        return getTemplate().opsForValue().getOperations().getExpire(key);
    }


}
