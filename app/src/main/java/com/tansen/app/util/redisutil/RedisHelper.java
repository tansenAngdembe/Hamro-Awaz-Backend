package com.tansen.app.util.redisutil;

import com.tansen.app.constant.RedisConstant;
import com.tansen.common.dto.SearchParam;
import com.tansen.entity.User;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RedisHelper {
    public static String buildCacheKey(Long municipalityId, SearchParam searchParam) {
        return RedisConstant.COMMENT_CACHE_KEY + municipalityId + ":" + searchParam.toCacheKey();
        // complaints:municipality:7:firstRow=0_pageSize=10_sortField=createdAt_sortOrder=asc_status=OPEN_priority=HIGH
    }
    public static String buildCommentCacheKey(String complaintUniqueId) {
        return  RedisConstant.COMMENT_CACHE_KEY + complaintUniqueId;
    }
    public static String buildNearComplaintCacheKey( SearchParam searchParam, BigDecimal latitude,
                                                    BigDecimal longitude,
                                                    Double radiusKm) {
        return RedisConstant.COMPLAINT_CACHE_KEY + ":" + searchParam.toCacheKey() + ":" + latitude + ":" + longitude + ":" + radiusKm;
    }

    public static boolean isComplaintLimitExceeded(User user, RedisTemplate<String,Object> redisTemplate) {
        String redisKey = RedisConstant.COMPLAINT_LIMIT + user.getId() + "::" + LocalDateTime.now();
        Long count = redisTemplate.opsForValue().increment(redisKey);

        if(count == null) {return true;}
        if(count == 1){
            LocalDateTime tomorrow = LocalDateTime.now().plusDays(1).toLocalDate().atStartOfDay();
            long seconds = Duration.between(LocalDateTime.now(),tomorrow).getSeconds();
            redisTemplate.expire(redisKey,Duration.ofSeconds(seconds));
        }
        return count > 4;

    }
    public static boolean isCooldownActive(User user, RedisTemplate<String,Object> redisTemplate) {
        String key = RedisConstant.COMPLAINT_COOLDOWN + user.getId() ;
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key,"LOCKED", Duration.ofMinutes(2));
        // if success == true → key was created → no cooldown
        // if success == false → key already exists → cooldown active
        return Boolean.FALSE.equals(success);
    }
}
