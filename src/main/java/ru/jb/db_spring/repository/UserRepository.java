package ru.jb.db_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jb.db_spring.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
