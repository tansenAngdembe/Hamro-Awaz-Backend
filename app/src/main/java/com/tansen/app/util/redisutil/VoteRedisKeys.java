package com.tansen.app.util.redisutil;

public class VoteRedisKeys {
    private VoteRedisKeys() {}

    // Stores total vote count: Integer
    public static String voteCount(Long complaintId) {
        return "complaint:" + complaintId + ":votes";
    }

    // Stores Set of userIds who voted
    public static String voterSet(Long complaintId) {
        return "complaint:" + complaintId + ":voters";
    }

    // Tracks which complaintIds have pending unsynced votes
    public static final String DIRTY_COMPLAINTS = "votes:dirty_complaints";
}
