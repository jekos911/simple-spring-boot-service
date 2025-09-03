package ru.jb.db_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.jb.db_spring.domain.Payment;

import java.util.List;

@Repository
public interface PaymentRepository  extends JpaRepository<Payment, Long> {
    List<Payment> findAllByUserId(Long userId);
}
