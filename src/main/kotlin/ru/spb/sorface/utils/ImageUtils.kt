package ru.spb.sorface.utils

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

}