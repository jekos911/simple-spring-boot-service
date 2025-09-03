package ru.jb.db_spring.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.jb.db_spring.api.dto.PaymentRequest;
import ru.jb.db_spring.api.dto.ProductDto;
import ru.jb.db_spring.client.ProductsClient;
import ru.jb.db_spring.domain.Payment;
import ru.jb.db_spring.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static ru.jb.db_spring.domain.PaymentStatus.CREATED;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ProductsClient productsClient;

    public PaymentService(PaymentRepository paymentRepository, ProductsClient productsClient) {
        this.paymentRepository = paymentRepository;
        this.productsClient = productsClient;
    }

    @Transactional
    public Payment execute(PaymentRequest paymentRequest) {
        if (paymentRequest.userId() == null || paymentRequest.userId() < 1) {
            throw new ResponseStatusException(BAD_REQUEST, "userId must be positive");
        }
        if (paymentRequest.productId() == null || paymentRequest.productId() < 1) {
            throw new ResponseStatusException(BAD_REQUEST, "productId must be positive");
        }
        if (paymentRequest.amount() == null || paymentRequest.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(BAD_REQUEST, "amount must be > 0");
        }

        ProductDto product = productsClient.findById(paymentRequest.productId());
        if (product.balance().compareTo(paymentRequest.amount()) < 0) {
            throw new ResponseStatusException(BAD_REQUEST, "Insufficient funds");
        }

        Payment p = new Payment();
        p.setUserId(paymentRequest.userId());
        p.setProductId(paymentRequest.productId());
        p.setAmount(paymentRequest.amount());
        p.setStatus(CREATED);

        return paymentRepository.save(p);
    }

    public Optional<Payment> findById(Long id) {
        if (id == null || id < 1) {
            throw new ResponseStatusException(BAD_REQUEST, "id must be positive");
        }
        return paymentRepository.findById(id);
    }

    public List<Payment> findAllByUserId(Long userId) {
        if (userId == null || userId < 1) {
            throw new ResponseStatusException(BAD_REQUEST, "userId must be positive");
        }
        return paymentRepository.findAllByUserId(userId);
    }
}
