package com.rock.micro.base.db.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis 服务基底
 */
public interface BaseRedisService {

    /**
     * 设置缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    boolean setTime(String key, long time);

    /**
     * 获取缓存失效时间
     *
     * @param key 键
     * @return 时间(秒)
     */
    long getTime(String key);

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    boolean containsKey(String key);

    /**
     * 删除
     *
     * @param key 可以传一个值 或多个
     */
    void delete(String... key);

    /**
     * 获取string
     *
     * @param key 键
     * @return 值
     */
    String getString(String key);

    /**
     * 存储
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    boolean set(String key, Object value);

    /**
     * 存储 设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置永久
     * @return true成功 false 失败
     */
    boolean setAndTime(String key, Object value, long time);

    /**
     * 分布式锁
     *
     * @param key  键
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置永久锁
     * @return
     */
    boolean lock(String key, long time);

    /**
     * 自增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    long incr(String key, long delta);

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    long decr(String key, long delta);

    /**
     * getHash某一个key的value
     *
     * @param key     键 不能为null
     * @param hashKey 项 不能为null
     * @return 值
     */
    Object getHash(String key, String hashKey);

    /**
     * getHash的所有key和value
     *
     * @param key 键
     * @return 对应的多个键值
     */
    Map<Object, Object> getHash(String key);

    /**
     * setHash 整个map
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    boolean setHash(String key, Map<String, Object> map);

    /**
     * setHash 整个map 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    boolean setHash(String key, Map<String, Object> map, long time);

    /**
     * setHash 单个key 不存在则创建
     *
     * @param key       键
     * @param hashKey   项
     * @param hashValue 值
     * @return true 成功 false失败
     */
    boolean setHash(String key, String hashKey, Object hashValue);

    /**
     * setHash 单个key 不存在则创建 并设置时间
     *
     * @param key       键
     * @param hashKey   项
     * @param hashValue 值
     * @param time      时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    boolean setHashAndTime(String key, String hashKey, Object hashValue, long time);

    /**
     * deleteHash 一个或多个值
     *
     * @param key      键 不能为null
     * @param hashKeys 项 可以使多个 不能为null
     */
    void deleteHash(String key, Object... hashKeys);

    /**
     * 判断hash中是否有该key
     *
     * @param key     键 不能为null
     * @param hashKey 项 不能为null
     * @return true 存在 false不存在
     */
    boolean containsHashKey(String key, String hashKey);

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key     键
     * @param hashKey 项
     * @param by      要增加几(大于0)
     * @return
     */
    double hashIncr(String key, String hashKey, double by);

    /**
     * hash递减
     *
     * @param key     键
     * @param hashKey 项
     * @param by      要减少记(小于0)
     * @return
     */
    double hashDecr(String key, String hashKey, double by);

    /**
     * getSet所有内容
     *
     * @param key 键
     * @return
     */
    Set<Object> getSet(String key);

    /**
     * set中是否存在某一个value
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    boolean containsSetValue(String key, Object value);

    /**
     * setSet
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    long setSet(String key, Object... values);

    /**
     * setSet并设置时间
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    long setSetAndTime(String key, long time, Object... values);


    /**
     * getSet的长度
     *
     * @param key 键
     * @return
     */
    long getSetSize(String key);

    /**
     * 删除set的某一个value
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    long deleteSet(String key, Object... values);

    /**
     * get List 所有内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    List<Object> getList(String key, long start, long end);

    /**
     * get List 长度
     *
     * @param key 键
     * @return
     */
    long getListSize(String key);

    /**
     * get List By index
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    Object getList(String key, long index);

    /**
     * list 左 pop 操作
     *
     * @param key 键
     * @return
     */
    String listLeftPop(String key);

    /**
     * list 右 pop 操作
     *
     * @param key 键
     * @return
     */
    String listRightPop(String key);

    /**
     * set list 的左边
     *
     * @param key   键
     * @param value 值
     * @return
     */
    boolean setListLeft(String key, Object value);

    /**
     * set list 的左边,并设置时间
     *
     * @param key   键
     * @param value 值
     * @return
     */
    boolean setListLeftAndTime(String key, Object value, long time);

    /**
     * set list 的左边
     *
     * @param key   键
     * @param value 值
     * @return
     */
    boolean setListLeft(String key, List<Object> value);

    /**
     * set list 的左边,并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    boolean setListLeftAndTime(String key, List<Object> value, long time);

    /**
     * set list 的右边
     *
     * @param key   键
     * @param value 值
     * @return
     */

    boolean setListRight(String key, Object value);

    /**
     * set list 的右边 并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */

    boolean setListRightAndTime(String key, Object value, long time);

    /**
     * set list 的右边
     *
     * @param key   键
     * @param value 值
     * @return
     */
    boolean setListRight(String key, List<Object> value);

    /**
     * set list 的右边 并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    boolean setListRightAndTime(String key, List<Object> value, long time);

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    boolean updateListIndex(String key, long index, Object value);

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    long deleteListIndex(String key, long count, Object value);

    /**
     * 移除index 开始结束的元素
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    void deleteListRangeIndex(String key, Long start, Long end);

}