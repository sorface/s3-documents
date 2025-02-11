package ru.spb.sorface.adviser

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

private val log = KotlinLogging.logger {}

@ControllerAdvice
class ControllerAdviser : ResponseEntityExceptionHandler() {
    @ExceptionHandler(RuntimeException::class)
    fun handleErrorResponseException(
        ex: RuntimeException,
        request: WebRequest,
    ): ResponseEntity<Nothing> {
        log.error { ex.message }
        when (ex) {
            is ResponseStatusException -> throw ex
            else -> throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.message)
        }
    }
}
