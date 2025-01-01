package com.yunting.Screenshot.config.minio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class MinioConfiguration {
    @Value("${MINIO_ROOT_END_POINT}")
    private String END_POINT;
    @Value("${MINIO_ROOT_USER}")
    private String USERNAME;
    @Value("${MINIO_ROOT_PASSWORD}")
    private String PASSWORD;

    @Bean("minioClient")
    public MinioClient minioClient() {
        return new MinioClient.Builder()
                .endpoint(END_POINT)
                .credentials(USERNAME, PASSWORD)
                .build();
    }
}
