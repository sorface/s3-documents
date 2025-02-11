package ru.spb.sorface.service

import java.io.InputStream

interface MinIOService {

    /**
     * Добавление объекта в S3
     *
     * @param contentType - тип объекта
     * @param content - байтовое предсталение объекта
     * @param path - путь по которому необходимо сохранить объект
     * @param bucket - бакет в s3 в котором объект будет сохранен
     * @throws - MinioErrorException
     */
    fun putObject(contentType: String, content: ByteArray, path: String, bucket: String)

    /**
     * Проверка существует ли объект в S3
     *
     * @param bucket - бакет в s3 в котором объект находится
     * @param path - путь до объекта
     * @return - true/false
     * @throws - MinioErrorException
     */
    fun isObjectExist(bucket: String, path: String): Boolean

    /**
     * Получение объекта из S3
     *
     * @param bucket - бакет в s3 в котором объект находится
     * @param path - путь до объекта
     * @return - байтовое предсталение объекта
     * @throws - MinioErrorException
     */
    fun getObject(bucket: String, path: String): InputStream

    /**
     * Удаление объекта из S3
     *
     * @param bucket - бакет в s3 в котором объект находится
     * @param path - путь до объекта
     * @throws - MinioErrorException
     */
    fun deleteObject(bucket: String, path: String)
}
