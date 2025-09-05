package ru.jb.db_spring.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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
import static ru.jb.db_spring.domain.PaymentStatus.FAILED;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ProductsClient productsClient;
    private final LimitService limitService;

    public PaymentService(PaymentRepository paymentRepository, ProductsClient productsClient, LimitService limitService) {
        this.paymentRepository = paymentRepository;
        this.productsClient = productsClient;
        this.limitService = limitService;
    }

    @Transactional
    public Payment execute(PaymentRequest paymentRequest) {
        if (!StringUtils.hasText(paymentRequest.externalId())) {
            throw new ResponseStatusException(BAD_REQUEST, "externalId is required");
        }

        var existing = paymentRepository.findByExternalId(paymentRequest.externalId());
        if (existing.isPresent()) {
            return existing.get();
        }

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

        boolean reserved = false;
        Payment p = new Payment();
        try {
            limitService.tryReserve(paymentRequest.userId(), paymentRequest.amount());
            reserved = true;
            ProductDto productDto = productsClient.findById(paymentRequest.productId());
            if (productDto.balance().compareTo(paymentRequest.amount()) < 0) {
                limitService.restore(paymentRequest.userId(), paymentRequest.amount());
                reserved = false;
                throw new ResponseStatusException(BAD_REQUEST, "Insufficient funds");
            }
            p.setUserId(paymentRequest.userId());
            p.setProductId(paymentRequest.productId());
            p.setAmount(paymentRequest.amount());
            p.setExternalId(paymentRequest.externalId());
            p.setStatus(CREATED);
            p = paymentRepository.save(p);
        } catch (RuntimeException e) {
            if (reserved) {
                limitService.restore(paymentRequest.userId(), paymentRequest.amount());
            }
            if (p != null && p.getId() != null && p.getStatus() != FAILED) {
                p.setStatus(FAILED);
                paymentRepository.save(p);
            }
            throw e;
        }

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
