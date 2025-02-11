package ru.spb.sorface.service.impl

import io.minio.GetObjectArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.RemoveObjectArgs
import io.minio.StatObjectArgs
import io.minio.errors.ErrorResponseException
import mu.KotlinLogging
import org.springframework.stereotype.Service
import ru.spb.sorface.exception.MinioErrorException
import ru.spb.sorface.service.MinIOService
import java.io.ByteArrayInputStream
import java.io.InputStream

private val log = KotlinLogging.logger {}

@Service
class MinIOServiceImpl(
    private val minioClient: MinioClient,
) : MinIOService {

    override fun isObjectExist(bucket: String, path: String): Boolean {
        log.info { "Start check isObjectExist path: $path, bucket: $bucket" }
        return runCatching {
            StatObjectArgs
                .builder()
                .bucket(bucket)
                .`object`(path)
                .build()
                .let { minioClient.statObject(it) }
        }.fold({ true }, {
            when (it) {
                is ErrorResponseException -> false
                else -> {
                    log.error(it) { it.message }
                    throw MinioErrorException("Непредвиденная ошибка при проверке наличия объекта в S3 path: $path, bucket: $bucket")
                }
            }
        })
    }

    override fun putObject(contentType: String, content: ByteArray, path: String, bucket: String) {
        log.info { "Started putObject in Minio to path: $path, bucket: $bucket" }
        runCatching {
            val contentInputStream: InputStream = ByteArrayInputStream(content)
            contentInputStream.use { input ->
                PutObjectArgs
                    .builder()
                    .bucket(bucket)
                    .`object`(convertPath(path))
                    .stream(input, input.available().toLong(), -1)
                    .contentType(contentType)
                    .build()
                    .let { minioClient.putObject(it) }
            }
        }.onSuccess {
            log.info { "Finished putObject in Minio to path: $path, bucket: $bucket" }
        }.onFailure {
            log.error(it) { it.message }
            throw MinioErrorException(it.message ?: "Непредвиденная ошибка при добавлении объекта в S3 path: $path, bucket: $bucket")
        }
    }

    override fun getObject(bucket: String, path: String): InputStream {
        log.info { "Start getObject path: $path, bucket: $bucket" }
        return runCatching {
            GetObjectArgs
                .builder()
                .bucket(bucket)
                .`object`(path)
                .build()
                .let { minioClient.getObject(it) }
        }.onSuccess { log.info { "Finished getDocument path: $path, bucket: $bucket" } }
            .onFailure {
                log.error(it) { it.message }
                throw MinioErrorException("Непредвиденная ошибка при получении объекта из S3 path: $path, bucket: $bucket")
            }.getOrThrow()
    }

    override fun deleteObject(bucket: String, path: String) {
        log.info { "Started deleteObject in Minio to path: $path, bucket - $bucket" }
        runCatching {
            RemoveObjectArgs
                .builder()
                .bucket(bucket)
                .`object`(convertPath(path))
                .build()
                .let {
                    minioClient.removeObject(it)
                }
        }.onSuccess {
            log.info { "Finished deleteObject in Minio to path: $path, bucket: $bucket" }
        }.onFailure {
            log.error(it) { it.message }
            throw MinioErrorException("Непредвиденная ошибка при удалении объекта из S3 path: $path, bucket: $bucket")
        }
    }

    private fun convertPath(path: String): String = if (path.startsWith("/")) path.substring(1) else path
}
