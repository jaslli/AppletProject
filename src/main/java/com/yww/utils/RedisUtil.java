package com.yww.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @ClassName RedisUtil
 * @Descriprtion Redis的封装工具类
 * @Author yww
 * @Date 2021/4/12 2:27
 * @Version 1.0
 **/
@Component
@SuppressWarnings(value = { "unchecked", "rawtypes" })
public class RedisUtil {

    @Resource
    private RedisTemplate redisTemplate;

    // *****************Zset****************************
    /**
     * 新增数据
     * @param sortedSetKey  Zset的Key，即用来区分不同的Zset
     * @param member        排序的对象
     * @param score         排序的对象的分数
     */
    public void addZset(String sortedSetKey, Object member, double score) {
        redisTemplate.opsForZSet().add(sortedSetKey, member, score);
    }

    /**
     * 获取排行榜前N的数据
     * @param sortedSetKey  Zset的Key，即用来区分不同的Zset
     * @param top        获取前top的数据
     * @return              排行榜前N的数据
     */
    public Set<ZSetOperations.TypedTuple<Object>> getTop(String sortedSetKey, long top) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(sortedSetKey, 0, top);
    }

    /**
     * 获取指定Zset中指定member的score值
     * @param sortedSetKey  Zset的Key，即用来区分不同的Zset
     * @param member        排序的对象
     * @return 返回指定对象的score值，若该member不存在，则返回null
     */
    public Double getScore(String sortedSetKey, Object member) {
        return redisTemplate.opsForZSet().score(sortedSetKey, member);
    }

    /**
     * 用于点击后为指定member的score增加值
     * @param sortedSetKey  Zset的Key，即用来区分不同的Zset
     * @param member        排序的对象
     */
    public void incrementScore(String sortedSetKey, Object member, double score) {
        redisTemplate.opsForZSet().incrementScore(sortedSetKey,member,score);
    }

    public void deletekey(String sortedSetKey,Object member) {
        redisTemplate.opsForZSet().remove(sortedSetKey,member);
    }

    // *****************bitMap****************************

    /**
     * 判断该位图是否存在
     * @param key   位图的KEY
     * @return      存在就返回true
     */
    public Boolean isExistBitMap(String key) {
        if (redisTemplate.opsForValue().size(key) == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 使用位图进行签到
     * @param key       位图的Key，格式为id + yyyy-MM
     * @param offset    位图偏移量，即日期减一
     */
    public void doSign(String key, int offset) {
        redisTemplate.opsForValue().setBit(key, offset, true);
    }

    /**
     * 检查用户是否签到
     * @param key       位图的Key，格式为id + yyyyMM
     * @param offset    位图偏移量，即日期减一
     * @return          签到的状态
     */
    public Boolean checkSign(String key, int offset) {
        return redisTemplate.opsForValue().getBit(key,offset);
    }

    /**
     * 统计该位图（该月）签到的次数
     * @param key   位图的Key，格式为id + yyyyMM
     * @return      该月签到的次数
     */
    public int getBitCount(String key) {
        int count = 0;
        int max = 32;
        for (int i = 1; i < max; i++) {
            if (checkSign(key,i)) {
                count++;
            }
        }
        return count;
    }

}
