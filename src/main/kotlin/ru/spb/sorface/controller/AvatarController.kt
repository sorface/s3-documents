package ru.spb.sorface.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import ru.spb.sorface.service.AvatarService

@RestController
@RequestMapping("/internal/document/s3/avatar")
class AvatarController(
    private val avatarService: AvatarService
) {

    @PostMapping("/{userId}")
    fun upload(@RequestParam("file") file: MultipartFile, @PathVariable userId: String) = avatarService.upload(file.contentType, file.bytes, userId)

    @GetMapping("/{userId}")
    fun get(@PathVariable userId: String, @RequestParam(required = false) imageSize: String? = null) =
        avatarService.get(userId, imageSize).let { ResponseEntity.ok().body(it) }

    @DeleteMapping("/{userId}")
    fun delete(@PathVariable userId: String): ResponseEntity<Nothing> = avatarService.delete(userId).let { ResponseEntity.noContent().build() }
}
