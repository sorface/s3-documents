package ru.spb.sorface.service.impl

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.web.server.ResponseStatusException
import ru.spb.sorface.config.S3DocumentProperties
import ru.spb.sorface.service.MinIOService
import java.nio.charset.Charset

@ExtendWith(MockitoExtension::class)
class AvatarServiceImplTest {
    @Mock
    lateinit var minIOService: MinIOService

    @Mock
    lateinit var s3DocumentProperties: S3DocumentProperties

    @InjectMocks
    lateinit var avatarServiceImpl: AvatarServiceImpl

    @Test
    fun upload() {
        val image = "image".toByteArray(Charset.defaultCharset())
        whenever(s3DocumentProperties.bucket).thenReturn("bucket")
        whenever(minIOService.isObjectExist(any(), any())).thenReturn(false)
        avatarServiceImpl.upload("image/png", image, "1")
        verify(minIOService, times(3)).isObjectExist(any(), any())
        verify(minIOService, times(3)).putObject(any(), any(), any(), any())
    }

    @Test
    fun uploadWithError() {
        val image = "image".toByteArray(Charset.defaultCharset())
        assertThrows<ResponseStatusException> { avatarServiceImpl.upload("application/json", image, "1") }
    }

    @Test
    fun get() {
        whenever(s3DocumentProperties.bucket).thenReturn("bucket")
        whenever(minIOService.isObjectExist(any(), any())).thenReturn(true)
        whenever(minIOService.getObject(any(), any())).thenReturn("image".toByteArray().inputStream())
        val actual = avatarServiceImpl.get("1", "small")
        Assertions.assertNotNull(actual)
        verify(minIOService).isObjectExist(any(), any())
        verify(minIOService).getObject(any(), any())
    }

    @Test
    fun getWithNoAvatar() {
        whenever(s3DocumentProperties.bucket).thenReturn("bucket")
        whenever(minIOService.isObjectExist(any(), any())).thenReturn(false)
        assertThrows<ResponseStatusException> { avatarServiceImpl.get("1", "small") }
    }

    @Test
    fun delete() {
        whenever(s3DocumentProperties.bucket).thenReturn("bucket")
        doNothing().whenever(minIOService).deleteObject(any(), any())
        avatarServiceImpl.delete("1")
        verify(minIOService, times(3)).deleteObject(any(), any())
    }
}
