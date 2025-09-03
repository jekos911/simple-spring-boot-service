package ru.jb.db_spring.api.mapper;

import ru.jb.db_spring.api.dto.ProductDto;
import ru.jb.db_spring.domain.Product;

public class ProductMapper {

    private ProductMapper() {};

    public static ProductDto toDto(Product product) {
        return new ProductDto(product.getId(),
                              product.getAccountNumber(),
                              product.getBalance(),
                              product.getType());
    }
}
