package ru.spb.sorface.config

import io.minio.MinioClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinIOConfig(
    private val s3DocumentProperties: S3DocumentProperties,
) {
    @Bean
    fun minioClient(): MinioClient =
        MinioClient
            .builder()
            .endpoint(s3DocumentProperties.minioHost)
            .credentials(s3DocumentProperties.minioAccessKey, s3DocumentProperties.minioSecretkey)
            .build()
}
