package ru.spb.sorface.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import ru.spb.sorface.security.jwt.SorfaceJwtAuthenticationConverter

@Configuration
class SecurityConfiguration {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .authorizeHttpRequests { httpRequestSpec -> httpRequestSpec.anyRequest().authenticated() }
            .oauth2ResourceServer { spec ->
                spec.jwt { jwtSpec ->
                    jwtSpec.jwtAuthenticationConverter(SorfaceJwtAuthenticationConverter())
                }
            }
            .cors { corsSpec -> corsSpec.disable() }
            .csrf { csrfSpec -> csrfSpec.disable() }
            .build()

}