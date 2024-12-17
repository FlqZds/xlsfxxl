package com.yunting.Screenshot.config.minio;

import io.minio.MinioClient;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class MinioConfiguration {

    private final String END_POINT = "http://192.168.99.11:9000";

    private final String USERNAME = "admin";

    private final String PASSWORD = "password";

    @Bean("minioClient")
    public MinioClient minioClient() {
        return new MinioClient.Builder()
                .endpoint(END_POINT)
                .credentials(USERNAME, PASSWORD)
                .build();
    }
}
