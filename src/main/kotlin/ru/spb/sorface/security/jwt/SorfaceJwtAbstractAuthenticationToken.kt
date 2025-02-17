package ru.spb.sorface.security.jwt

import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

/**
 * Класс, представляющий токен аутентификации на основе JWT.
 */
class SorfaceJwtAbstractAuthenticationToken(private val jwt: Jwt) : JwtAuthenticationToken(jwt, SorfaceJwtConverters.convertAuthorities(jwt), jwt.subject) {

    /**
     * Возвращает атрибуты токена.
     *
     * @return атрибуты токена.
     */
    override fun getTokenAttributes(): MutableMap<String, Any> = (token as Jwt).claims

    /**
     * Возвращает субъект токена.
     *
     * @return субъект токена.
     */
    override fun getPrincipal(): Any = SorfaceJwtConverters.convertPrincipal(jwt)

}