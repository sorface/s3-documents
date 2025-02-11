package ru.spb.sorface.service

interface AvatarService {

    /**
     * Загрузка аватара в S3 в 3 размерах исходный(normal)/64x64(small)/256x256(medium)
     *
     * @param contentType - тип изображения
     * @param content - байтовое предсталение изображения
     * @param userId - id пользака
     */
    fun upload(contentType: String?, content: ByteArray, userId: String)

    /**
     * Получение аватара из S3 в 3 размерах исходный(normal)/64x64(small)/256x256(medium)
     *
     * @param userId - id пользака
     * @param imageSize - исходный(normal)/64x64(small)/256x256(medium)
     * @return - байтовое предсталение изображения
     */
    fun get(userId: String, imageSize: String? = null): ByteArray

    /**
     * Удаление аватара из S3
     *
     * @param userId - id пользака
     */
    fun delete(userId: String)
}
