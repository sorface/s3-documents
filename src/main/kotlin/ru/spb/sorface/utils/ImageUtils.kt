package ru.spb.sorface.utils

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.server.ResponseStatusException
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

/**
 * Утилитный класс для работы с изображениями.
 */
object ImageUtils {

    /**
     * Метод для изменения размера изображения.
     *
     * @param originalImage Исходное изображение в виде массива байтов.
     * @param imageType Тип изображения (например, "jpg", "png").
     * @param targetWidth Желаемая ширина изображения.
     * @param targetHeight Желаемая высота изображения.
     * @return Измененное изображение в виде массива байтов.
     */
    fun resize(originalImage: ByteArray, imageType: String, targetWidth: Int?, targetHeight: Int?): ByteArray {
        if (targetWidth == null || targetHeight == null) {
            return originalImage
        }

        val resizedImage = BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB)

        resizedImage
            .createGraphics()
            .also { it.drawImage(ImageIO.read(originalImage.inputStream()), 0, 0, targetWidth, targetHeight, null) }
            .also { it.dispose() }

        val byteArrayOutputStream = ByteArrayOutputStream()

        resizedImage.let { ImageIO.write(it, imageType, byteArrayOutputStream) }

        return byteArrayOutputStream.use { it.toByteArray() }
    }

    // Define the magic numbers for common image formats
    private val JPEG_SIGNATURE = byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte())
    private val PNG_SIGNATURE = byteArrayOf(0x89.toByte(), 0x50.toByte(), 0x4E.toByte(), 0x47.toByte(), 0x0D.toByte(), 0x0A.toByte(), 0x1A.toByte(), 0x0A.toByte())
    private val GIF_SIGNATURE = byteArrayOf(0x47.toByte(), 0x49.toByte(), 0x46.toByte())

    /**
     * Метод для получения типа изображения.
     *
     * @param imageData - изображение в виде массива байтов.
     * @return - Медиа тип изображения
     * @throws ResponseStatusException - если тип изображения не установлен
     */
    fun getImageType(imageData: ByteArray): MediaType {
        return when {
            imageData.copyOfRange(0, JPEG_SIGNATURE.size).contentEquals(JPEG_SIGNATURE) -> MediaType.IMAGE_JPEG

            imageData.copyOfRange(0, PNG_SIGNATURE.size).contentEquals(PNG_SIGNATURE) -> MediaType.IMAGE_PNG

            imageData.size >= 6 && imageData.copyOfRange(0, GIF_SIGNATURE.size).contentEquals(GIF_SIGNATURE) -> MediaType.IMAGE_GIF

            else -> throw ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
        }
    }

}