package com.tansen.government.util.redisutil;

import com.tansen.common.dto.SearchParam;
import com.tansen.government.constant.RedisConstant;

public class RedisHelper {

    /**
     * Specific cache key for a complaint list query.
     * Example: "complaints:municipality:7:firstRow=0_pageSize=10_sortField=createdAt_sortOrder=asc"
     */
    public static String buildCacheKey(Long municipalityId, SearchParam searchParam) {
        return RedisConstant.COMPLAINT_CACHE_KEY + municipalityId + ":" + searchParam.toCacheKey();
    }

    /**
     * Wildcard pattern to invalidate ALL cached complaint pages for a municipality.
     * Example: "complaints:municipality:7:*"
     * Must match the prefix used in buildCacheKey exactly.
     */
    public static String buildCacheKeyPattern(Long municipalityId) {
        return RedisConstant.COMPLAINT_CACHE_KEY + municipalityId + ":*";
        // ✅ "complaints:municipality:7:*" — now matches buildCacheKey output
    }

    /**
     * Cache key for comments of a specific complaint.
     * Example: "comments:complaint:COMP-001"
     */
    public static String buildCommentCacheKey(String complaintUniqueId) {
        return RedisConstant.COMMENT_CACHE_KEY + complaintUniqueId;
    }
}
