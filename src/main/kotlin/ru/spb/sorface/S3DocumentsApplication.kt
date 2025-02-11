package ru.spb.sorface

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import ru.spb.sorface.config.S3DocumentProperties

@EnableConfigurationProperties(value = [S3DocumentProperties::class])
@SpringBootApplication
class S3DocumentsApplication

fun main(args: Array<String>) {
    runApplication<S3DocumentsApplication>(*args)
}
