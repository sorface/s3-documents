package ru.spb.sorface.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import ru.spb.sorface.security.principal.SorfacePrincipal
import ru.spb.sorface.service.AvatarService
import ru.spb.sorface.utils.ImageUtils

@RestController
@RequestMapping("/internal/document/s3/avatar")
class AvatarController(
    private val avatarService: AvatarService
) {

    @PostMapping
    fun upload(@RequestParam("file") file: MultipartFile, @AuthenticationPrincipal principal: SorfacePrincipal) {
        return avatarService.upload(file.contentType, file.bytes, principal.id.toString())
    }

    @GetMapping("/{userId}")
    fun get(@PathVariable userId: String, @RequestParam(required = false) imageSize: String? = null) : ResponseEntity<ByteArray> =
        avatarService.get(userId, imageSize).let { ResponseEntity.ok().contentType(ImageUtils.getImageType(it)).body(it) }

    @DeleteMapping("/{userId}")
    fun delete(@PathVariable userId: String): ResponseEntity<Nothing> = avatarService.delete(userId).let { ResponseEntity.noContent().build() }
}
