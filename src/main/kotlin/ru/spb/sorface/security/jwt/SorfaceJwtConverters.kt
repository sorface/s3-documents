package ru.spb.sorface.security.jwt

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import ru.spb.sorface.security.principal.SorfacePrincipal
import java.security.Principal
import java.util.*


/**
 * Объект, содержащий методы для конвертации JWT в объекты GrantedAuthority и Principal.
 */
object SorfaceJwtConverters {

    /**
     * Префикс для ролей.
     */
    private const val ROLE_PREFIX = "ROLE"

    /**
     * Префикс для областей.
     */
    private const val SCOPE_PREFIX = "SCOPE"

    /**
     * Имя поля в JWT, содержащего идентификатор субъекта.
     */
    private const val PRINCIPAL_ID_CLAIM = "principal-id"

    /**
     * Конвертирует JWT в список полномочий.
     *
     * @param jwt JWT для конвертации.
     * @return список полномочий.
     */
    fun convertAuthorities(jwt: Jwt): List<GrantedAuthority> {
        val roles = (jwt.claims["roles"] as List<String>? ?: listOf())
            .map { role ->
                SimpleGrantedAuthority("${ROLE_PREFIX}_$role")
            }.toMutableList()

        val scopes = (jwt.claims["scope"] as List<String>? ?: listOf())
            .map { role ->
                SimpleGrantedAuthority("${SCOPE_PREFIX}_$role")
            }
            .toMutableList()

        return roles + scopes
    }

    /**
     * Конвертирует JWT в объект Principal.
     *
     * @param jwt JWT для конвертации.
     * @return объект Principal.
     */
    fun convertPrincipal(jwt: Jwt): Principal = SorfacePrincipal()
        .apply {
            _name = jwt.subject
            id = (jwt.claims[PRINCIPAL_ID_CLAIM] as String).let { UUID.fromString(it) }
            authorities = convertAuthorities(jwt)
        }

}