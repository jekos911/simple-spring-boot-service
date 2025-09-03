package ru.jb.db_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jb.db_spring.domain.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByUserId(Long userId);
}
