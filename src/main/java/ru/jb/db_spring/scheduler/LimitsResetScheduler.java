package ru.jb.db_spring.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.jb.db_spring.service.impl.LimitService;

@Component
public class LimitsResetScheduler {
    private static final Logger log = LoggerFactory.getLogger(LimitsResetScheduler.class);
    private final LimitService limitService;

    public LimitsResetScheduler(LimitService limitService) {
        this.limitService = limitService;
    }

    public void resetDailyLimits() {
        log.info("Resetting daily limits");
        limitService.prepareTodayForAllKnownClients();
        log.info("Daily limits prepared");
    }
}
