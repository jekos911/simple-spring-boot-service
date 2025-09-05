package ru.jb.db_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.jb.db_spring.domain.Limit;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LimitsRepository extends JpaRepository<Limit, Long> {
    Optional<Limit> findByClientIdAndDay(Long clientId, LocalDate day);

    @Query("select distinct l.clientId from Limit l")
    List<Long> findDistinctClientIds();
}
