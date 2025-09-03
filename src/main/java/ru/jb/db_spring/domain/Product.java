package ru.jb.db_spring.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "products"
)
public class Product {

       @Id
       @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
       @SequenceGenerator(name = "product_seq", sequenceName = "product_seq")
       private Long id;

       @NotBlank
       @Column(name = "account_number", nullable = false, unique = true, length = 32)
       private String accountNumber;

       @NotNull
       @Column(nullable = false, precision = 19, scale = 2)
       private BigDecimal balance = BigDecimal.ZERO;

       @NotBlank
       @Column(nullable = false, length = 20)
       private String type;

       @CreationTimestamp
       @Column(nullable = false, updatable = false)
       private Instant createdAt;

       @UpdateTimestamp
       @Column(nullable = false)
       private Instant updatedAt;

       @ManyToOne(fetch = FetchType.LAZY, optional = false)
       @JoinColumn(name = "user_id", nullable = false,
                   foreignKey = @ForeignKey(name = "fk_products_user"))
       private User user;

       public Long getId() {
              return id;
       }

       public void setId(Long id) {
              this.id = id;
       }

       public String getAccountNumber() {
              return accountNumber;
       }

       public void setAccountNumber(String accountNumber) {
              this.accountNumber = accountNumber;
       }

       public BigDecimal getBalance() {
              return balance;
       }

       public void setBalance(BigDecimal balance) {
              this.balance = balance;
       }

       public String getType() {
              return type;
       }

       public void setType(String type) {
              this.type = type;
       }

       public Instant getCreatedAt() {
              return createdAt;
       }

       public void setCreatedAt(Instant createdAt) {
              this.createdAt = createdAt;
       }

       public Instant getUpdatedAt() {
              return updatedAt;
       }

       public void setUpdatedAt(Instant updatedAt) {
              this.updatedAt = updatedAt;
       }

       public User getUser() {
              return user;
       }

       public void setUser(User user) {
              this.user = user;
       }
}
