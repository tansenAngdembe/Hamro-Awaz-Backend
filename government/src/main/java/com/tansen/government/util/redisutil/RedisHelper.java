package com.tansen.government.util.redisutil;

import com.tansen.common.dto.SearchParam;
import com.tansen.government.constant.RedisConstant;

public class RedisHelper {
    public static String buildCacheKey(Long municipalityId, SearchParam searchParam) {
        return RedisConstant.COMPLAINT_CACHE_KEY + municipalityId + ":" + searchParam.toCacheKey();
        // complaints:municipality:7:firstRow=0_pageSize=10_sortField=createdAt_sortOrder=asc_status=OPEN_priority=HIGH
    }
    public static String buildCommentCacheKey(String complaintUniqueId) {
        return  RedisConstant.COMPLAINT_CACHE_KEY + complaintUniqueId;
    }
}
