package com.rock.micro.base.db.redis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public final class BaseRedisServiceImpl implements BaseRedisService {

    private static final Logger logger = LoggerFactory.getLogger(BaseRedisServiceImpl.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean setTime(String key, long time) {
        try {
            if (time > 0L) {
                //设置过期时间
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            } else if (time == -1L) {
                //移除过期时间
                redisTemplate.persist(key);
            }
            return true;
        } catch (Exception e) {
            logger.error(" redis setTime fail:{}", e);
            return false;
        }
    }

    @Override
    public long getTime(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public boolean containsKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            logger.error(" redis containsKey fail:{}", e);
            return false;
        }
    }

    @Override
    public void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }

    @Override
    public String getString(String key) {
        //判空
        if (StringUtils.isBlank(key)) {
            //过
            return null;
        }
        //结果
        Object value = redisTemplate.opsForValue().get(key);
        //判空
        if (value == null) {
            //过
            return null;
        }
        //查询返回
        return value.toString();
    }

    @Override
    public boolean set(String key, Object value) {
        return setAndTime(key, value, -1L);
    }

    @Override
    public boolean setAndTime(String key, Object value, long time) {
        try {
            if (time > 0L) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(key, value);
            }
            return true;
        } catch (Exception e) {
            logger.error(" redis setAndTime fail:{}", e);
            return false;
        }
    }

    @Override
    public boolean lock(String key, long time) {
        //锁是否成功,默认不成功
        boolean lock = false;
        try {
            //尝试设置值,如果没有值,才能设置成功,否则设置失败,如果成功,说明获取锁
            lock = redisTemplate.opsForValue().setIfAbsent(key, String.format("我是分布式锁,过期时间:%s秒", time), time, TimeUnit.SECONDS);
        } catch (Exception e) {

        }
        //返回结果
        return lock;
    }


    @Override
    public long incr(String key, long delta) {
        if (delta < 0L) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public long decr(String key, long delta) {
        if (delta < 0L) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    @Override
    public Object getHash(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    public Map<Object, Object> getHash(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    @Override
    public boolean setHash(String key, Map<String, Object> map) {
        return setHash(key, map, -1L);
    }

    @Override
    public boolean setHash(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            //如果需要设置时间
            if (time > 0L) {
                //设置时间
                setTime(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error(" redis setHash fail:{}", e);
            return false;
        }
    }

    @Override
    public boolean setHash(String key, String hashKey, Object hashValue) {
        return setHashAndTime(key, hashKey, hashValue, -1L);
    }

    @Override
    public boolean setHashAndTime(String key, String hashKey, Object hashValue, long time) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, hashValue);
            //如果需要设置时间
            if (time > 0L) {
                //设置时间
                setTime(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error(" redis setHashAndTime fail:{}", e);
            return false;
        }
    }

    @Override
    public void deleteHash(String key, Object... hashKeys) {
        redisTemplate.opsForHash().delete(key, hashKeys);
    }

    @Override
    public boolean containsHashKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    @Override
    public double hashIncr(String key, String hashKey, double by) {
        return redisTemplate.opsForHash().increment(key, hashKey, by);
    }

    @Override
    public double hashDecr(String key, String hashKey, double by) {
        return redisTemplate.opsForHash().increment(key, hashKey, -by);
    }

    @Override
    public Set<Object> getSet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            logger.error(" redis getSet fail:{}", e);
            return null;
        }
    }

    @Override
    public boolean containsSetValue(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            logger.error(" redis containsSetValue fail:{}", e);
            return false;
        }
    }

    @Override
    public long setSet(String key, Object... values) {
        return setSetAndTime(key, -1L, values);
    }

    @Override
    public long setSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            //如果需要设置时间
            if (time > 0L) {
                //设置时间
                setTime(key, time);
            }
            //判空
            if (count == null) {
                return 0L;
            }
            return count.longValue();
        } catch (Exception e) {
            logger.error(" redis setSet fail:{}", e);
            return 0L;
        }
    }

    @Override
    public long getSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            logger.error(" redis getSetSize fail:{}", e);
            return 0L;
        }
    }

    @Override
    public long deleteSet(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            //判空
            if (count == null) {
                return 0L;
            }
            return count;
        } catch (Exception e) {
            logger.error(" redis deleteSet fail:{}", e);
            return 0L;
        }
    }

    @Override
    public List<Object> getList(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            logger.error(" redis getList fail:{}", e);
            return null;
        }
    }

    @Override
    public long getListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            logger.error(" redis getListSize fail:{}", e);
            return 0L;
        }
    }

    @Override
    public Object getList(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            logger.error(" redis getList fail:{}", e);
            return null;
        }
    }

    @Override
    public String listLeftPop(String key) {
        try {
            return redisTemplate.opsForList().leftPop(key).toString();
        } catch (Exception e) {
            logger.error(" redis listLeftPop fail:{}", e);
            return null;
        }
    }

    @Override
    public String listRightPop(String key) {
        try {
            return redisTemplate.opsForList().rightPop(key).toString();
        } catch (Exception e) {
            logger.error(" redis listLeftPop fail:{}", e);
            return null;
        }
    }

    @Override
    public boolean setListLeft(String key, Object value) {
        return setListLeftAndTime(key, value, -1L);
    }

    @Override
    public boolean setListLeftAndTime(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            if (time > 0) {
                setTime(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error(" redis setListLeftAndTime fail:{}", e);
            return false;
        }
    }

    @Override
    public boolean setListLeft(String key, List<Object> value) {
        return setListLeftAndTime(key, value, -1L);
    }

    @Override
    public boolean setListLeftAndTime(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().leftPushAll(key, value);
            if (time > 0) {
                setTime(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error(" redis setListLeftAndTime fail:{}", e);
            return false;
        }
    }

    @Override
    public boolean setListRight(String key, Object value) {
        return setListRightAndTime(key, value, -1L);
    }

    @Override
    public boolean setListRightAndTime(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                setTime(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error(" redis setListRightAndTime fail:{}", e);
            return false;
        }
    }

    @Override
    public boolean setListRight(String key, List<Object> value) {
        return setListRightAndTime(key, value, -1L);
    }

    @Override
    public boolean setListRightAndTime(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                setTime(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error(" redis setListRightAndTime fail:{}", e);
            return false;
        }
    }

    @Override
    public boolean updateListIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            logger.error(" redis updateListIndex fail:{}", e);
            return false;
        }
    }

    @Override
    public long deleteListIndex(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            if (remove == null) {
                return 0L;
            }
            return remove;
        } catch (Exception e) {
            logger.error(" redis deleteListIndex fail:{}", e);
            return 0L;
        }
    }

    @Override
    public void deleteListRangeIndex(String key, Long start, Long end) {
        try {
            redisTemplate.opsForList().trim(key, start, end);
        } catch (Exception e) {
            logger.error(" redis deleteListRangeIndex fail:{}", e);
        }
    }

}