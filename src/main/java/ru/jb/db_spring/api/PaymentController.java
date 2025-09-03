package ru.jb.db_spring.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.jb.db_spring.api.dto.BalanceSummaryDto;
import ru.jb.db_spring.api.dto.PaymentRequest;
import ru.jb.db_spring.api.dto.ProductDto;
import ru.jb.db_spring.client.ProductsClient;
import ru.jb.db_spring.domain.Payment;
import ru.jb.db_spring.service.impl.PaymentService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/payments")
@Validated
public class PaymentController {
    private final ProductsClient productsClient;
    private final PaymentService paymentService;

    public PaymentController(ProductsClient productsClient, PaymentService paymentService) {
        this.productsClient = productsClient;
        this.paymentService = paymentService;
    }

    @GetMapping("/products")
    public List<ProductDto> productsByUser(@RequestParam @NotNull @Min(1) Long userId) {
        return productsClient.findByUser(userId);
    }

    @GetMapping
    public ProductDto productById(@RequestParam @NotNull @Min(1) Long id) {
        return productsClient.findById(id);
    }

    @GetMapping("/balances/summary")
    public BalanceSummaryDto balanceSummary(@RequestParam @NotNull @Min(1) Long userId) {
        var products = productsClient.findByUser(userId);

        var total = products.stream()
                .map(ProductDto::balance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var byType = products.stream()
                .collect(Collectors.groupingBy(
                        ProductDto::type,
                        Collectors.mapping(ProductDto::balance,
                                           Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        return new BalanceSummaryDto(userId, total, byType);
    }

    @PostMapping("/pay")
    @ResponseStatus(HttpStatus.CREATED)
    public Payment pay(@RequestBody PaymentRequest request) {
        return paymentService.execute(request);
    }

    @GetMapping("/{id}")
    public Payment getById(@PathVariable @NotNull @Min(1) Long id) {
        return paymentService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,"Payment not found"));
    }

    @GetMapping("/by-user")
    public List<Payment> byUser(@RequestParam @NotNull @Min(1) Long userId) {
        return paymentService.findAllByUserId(userId);
    }
}
