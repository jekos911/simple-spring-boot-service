package ru.jb.db_spring.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import ru.jb.db_spring.api.dto.ProductDto;

import java.util.List;

@Component
public class ProductsClient {
    private final RestClient restClient;

    public ProductsClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<ProductDto> findByUser(Long userId) {
        try {
            return restClient.get()
                    .uri(uri -> uri.queryParam("userId", userId).build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<ProductDto>>() {});
        } catch (HttpClientErrorException e) {
            throw e;
        } catch (RestClientException e) {
            throw e;
        }
    }

    public ProductDto findById(Long id) {
        try {
            return restClient.get()
                    .uri("/{id}", id)
                    .retrieve()
                    .body(ProductDto.class);
        } catch (HttpClientErrorException e) {
            throw e;
        } catch (RestClientException e) {
            throw e;
        }
    }
}
