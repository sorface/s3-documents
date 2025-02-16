package ru.spb.sorface.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "application.metadata")
data class ApplicationMetadata(val version: String)