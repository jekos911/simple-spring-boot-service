package ru.jb.db_spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "payments")
public class PaymentsProperties {
    private String productsBaseUrl;
    private Http http = new Http();

    public String getProductsBaseUrl() {
        return productsBaseUrl;
    }

    public void setProductsBaseUrl(String productsBaseUrl) {
        this.productsBaseUrl = productsBaseUrl;
    }

    public Http getHttp() {
        return http;
    }

    public void setHttp(Http http) {
        this.http = http;
    }

    public static class Http {
        private int connectionTimeoutMs = 1500;
        private int readTimeoutMs = 2500;

        public int getConnectionTimeoutMs() {
            return connectionTimeoutMs;
        }

        public void setConnectionTimeoutMs(int connectionTimeoutMs) {
            this.connectionTimeoutMs = connectionTimeoutMs;
        }

        public int getReadTimeoutMs() {
            return readTimeoutMs;
        }

        public void setReadTimeoutMs(int readTimeoutMs) {
            this.readTimeoutMs = readTimeoutMs;
        }
    }
}
