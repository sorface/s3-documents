package ru.spb.sorface.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "sorface.document")
data class S3DocumentProperties(
    var minioHost: String = "",
    var minioAccessKey: String = "",
    var minioSecretkey: String = "",
    var bucket: String = "",
)
