package com.tansen.common.dto;

public class EmptyComplaintStats implements ComplaintStats {

    public long getTotal() { return 0; }
    public long getResolved() { return 0; }
    public long getPending() { return 0; }
    public long getInProgress() { return 0; }
    public long getEscalated() { return 0; }

    public long getTotalComments() { return 0; }
    public long getTotalVotes() { return 0; }
    public long getUpVotes() { return 0; }
    public long getDownVotes() { return 0; }

    public Long administrativeId() {return null;};

    public long getSlaBreached() { return 0; }
    public double getEscalationRate() { return 0; }
}