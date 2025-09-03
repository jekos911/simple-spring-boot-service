package ru.jb.db_spring.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(PaymentsProperties.class)
public class HttpClientsConfig {

    @Bean
    public RestClient paymentRestClient(PaymentsProperties properties) {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(properties.getHttp().getConnectionTimeoutMs());
        factory.setReadTimeout(properties.getHttp().getReadTimeoutMs());

        return RestClient.builder()
                .requestFactory(factory)
                .baseUrl(properties.getProductsBaseUrl())
                .build();
    }
}
