package ru.spb.sorface.security.principal

import org.springframework.security.core.GrantedAuthority
import java.security.Principal
import java.util.UUID

/**
 * Класс, представляющий субъект в системе безопасности Sorface.
 */
class SorfacePrincipal : Principal {

    /**
     * Уникальный идентификатор субъекта.
     */
    var id: UUID? = null

    /**
     * Имя субъекта.
     */
    var _name: String? = null

    /**
     * Список полномочий, предоставленных субъекту.
     */
    var authorities: Collection<GrantedAuthority> = listOf()

    /**
     * Возвращает имя субъекта.
     *
     * @return имя субъекта
     */
    override fun getName(): String = this._name!!

}