package com.tansen.common.dto;

public interface ComplaintStats {

    long getTotal();
    long getResolved();
    long getPending();
    long getInProgress();
    long getEscalated();

    long getTotalComments();
    long getTotalVotes();
    long getUpVotes();
    long getDownVotes();

    Long administrativeId();

    long getSlaBreached();
    double getEscalationRate();
}
