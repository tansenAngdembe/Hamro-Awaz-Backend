package com.tansen.government.util.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EscalationScheduler {
    @Scheduled(fixedRate = 70000)
    public void escalate() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(5);

    }
}
