package ru.spb.sorface.service.impl

import kotlinx.coroutines.*
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import ru.spb.sorface.config.S3DocumentProperties
import ru.spb.sorface.service.AvatarService
import ru.spb.sorface.service.MinIOService
import ru.spb.sorface.utils.ImageUtils

@Service
class AvatarServiceImpl(
    private val minIOService: MinIOService,
    private val s3DocumentProperties: S3DocumentProperties,
) : AvatarService {

    private val normalPathTemplate: Pair<(String) -> String, Int?> = { userId: String -> "user-content/$userId/normal/avatar" } to null
    private val smallPathTemplate: Pair<(String) -> String, Int?> = { userId: String -> "user-content/$userId/small/avatar" } to 64
    private val mediumPathTemplate: Pair<(String) -> String, Int?> = { userId: String -> "user-content/$userId/medium/avatar" } to 256

    private val templateList = listOf(normalPathTemplate, smallPathTemplate, mediumPathTemplate)

    override fun upload(contentType: String?, content: ByteArray, userId: String) = runBlocking {
        withContext(CoroutineName("image upload") + Dispatchers.IO) {
            if (contentType == null || !contentType.startsWith("image")) {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "not supported content type [$contentType]")
            }

            val imageType = contentType.substring(contentType.indexOf("/") + 1)

            for (template in templateList) {
                launch {
                    val specialPath = template.first(userId)

                    if (minIOService.isObjectExist(s3DocumentProperties.bucket, specialPath)) {
                        minIOService.deleteObject(s3DocumentProperties.bucket, specialPath)
                    }

                    val resizeContent = ImageUtils.resize(content, imageType, template.second, template.second)

                    minIOService.putObject(contentType, resizeContent, specialPath, s3DocumentProperties.bucket)
                }
            }
        }
    }

    override fun get(userId: String, imageSize: String?): ByteArray {
        val path = when (imageSize) {
            "small" -> smallPathTemplate
            "medium" -> mediumPathTemplate
            else -> normalPathTemplate
        }.first(userId)

        return if (minIOService.isObjectExist(s3DocumentProperties.bucket, path)) {
            minIOService.getObject(s3DocumentProperties.bucket, path).readBytes()
        } else {
            throw ResponseStatusException(HttpStatus.NO_CONTENT)
        }
    }

    override fun delete(userId: String) = runBlocking {
        withContext(CoroutineName("image upload") + Dispatchers.IO) {
            for (template in templateList) {
                launch {
                    val specialPath = template.first(userId)
                    minIOService.deleteObject(s3DocumentProperties.bucket, specialPath)
                }
            }
        }
    }


}
