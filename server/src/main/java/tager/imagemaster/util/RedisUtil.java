package tager.imagemaster.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("unchecked")
@Component
public class RedisUtil {
    @SuppressWarnings("rawtypes")
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

//    /**
//     * 批量删除对应的value
//     *
//     * @param keys
//     */
//    public void remove(final String... keys) {
//        for (String key : keys) {
//            remove(key);
//        }
//    }

//    /**
//     * 批量删除key
//     *
//     * @param pattern
//     */
//    public void removePattern(final String pattern) {
//        Set<String> keys = redisTemplate.keys(pattern);
//        if (keys.size() > 0)
//            redisTemplate.delete(keys);
//    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object get(final String key) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        Object result = operations.get(key);
        return result;
    }

//    /**
//     * 写入缓存
//     *
//     * @param key
//     * @param value
//     * @return
//     */
//    public boolean set(final String key, UserInfo value) {
//        boolean result = false;
//        try {
//            result = set(key, value, 1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, int expireTime) {
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            operations.getOperations().expire(key, expireTime, TimeUnit.MINUTES);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean refresh(final String key, int expireTime) {
        return set(key, get(key), expireTime);
    }

//    public boolean setList(final String key, List list, int expireTime) {
//        boolean result = false;
//        try {
//            ListOperations operations = redisTemplate.opsForList();
//
//            operations.leftPush(key, list);
//            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
//
//            result = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }
//
//    public void flushdb() {
//        redisTemplate.execute((RedisCallback<Object>) connection -> {
//            connection.flushDb();
//            return "ok";
//        });
//    }
}

