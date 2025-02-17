package ru.spb.sorface.security.jwt

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.oauth2.jwt.Jwt

/**
 * Класс, реализующий конвертер JWT в объект AbstractAuthenticationToken.
 */
class SorfaceJwtAuthenticationConverter : Converter<Jwt, AbstractAuthenticationToken> {

    /**
     * Конвертирует JWT в объект AbstractAuthenticationToken.
     *
     * @param jwt JWT для конвертации.
     * @return объект AbstractAuthenticationToken.
     */
    override fun convert(jwt: Jwt): AbstractAuthenticationToken = SorfaceJwtAbstractAuthenticationToken(jwt)

}
