package ru.jb.db_spring.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jb.db_spring.api.LimitExceededException;
import ru.jb.db_spring.config.LimitsProperties;
import ru.jb.db_spring.domain.Limit;
import ru.jb.db_spring.repository.LimitsRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class LimitService {
    private final LimitsRepository limitsRepository;
    private final LimitsProperties limitsProperties;

    public LimitService(LimitsRepository limitsRepository, LimitsProperties limitsProperties) {
        this.limitsRepository = limitsRepository;
        this.limitsProperties = limitsProperties;
    }

    @Transactional
    public Limit ensure(Long clientId, LocalDate day) {
        return limitsRepository.findByClientIdAndDay(clientId, day)
                .orElseGet(() -> {
                    Limit l = new Limit();
                    l.setClientId(clientId);
                    l.setDay(day);
                    l.setRemaining(limitsProperties.getDefaultAmount());
                    return limitsRepository.save(l);
                });
    }

    @Transactional
    public Limit tryReserve(Long clientId, BigDecimal amount) {
        LocalDate today = LocalDate.now();
        Limit l = ensure(clientId, today);

        if (l.getRemaining().compareTo(amount) < 0) {
            throw new LimitExceededException("Daily limit exceeded for clientId = " + clientId);
        }
        l.setRemaining(l.getRemaining().subtract(amount));
        return limitsRepository.save(l);
    }

    @Transactional
    public Limit restore(Long clientId, BigDecimal amount) {
        LocalDate today = LocalDate.now();
        Limit l = ensure(clientId, today);
        l.setRemaining(l.getRemaining().add(amount));
        return limitsRepository.save(l);
    }

    @Transactional
    public void prepareTodayForAllKnownClients() {
        List<Long> clients = limitsRepository.findDistinctClientIds();
        LocalDate today = LocalDate.now();
        for (Long clientId: clients) {
            Limit l = limitsRepository.findByClientIdAndDay(clientId, today).orElse(null);
            if (l == null) {
                ensure(clientId, today);
            } else {
                l.setRemaining(limitsProperties.getDefaultAmount());
                limitsRepository.save(l);
            }
        }
    }
}
