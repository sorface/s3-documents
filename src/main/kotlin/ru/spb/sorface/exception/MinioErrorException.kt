package ru.spb.sorface.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class MinioErrorException(
    message: String,
    httpStatus: HttpStatus? = null,
) : ResponseStatusException(
    httpStatus ?: HttpStatus.INTERNAL_SERVER_ERROR,
    message,
)
