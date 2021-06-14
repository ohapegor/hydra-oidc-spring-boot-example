package ru.ohapegor.oidc.hydraclient.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter


@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests { authorizeRequests ->
            authorizeRequests
                    .anyRequest().authenticated()
        }
                .oauth2Login { oauth2Login ->
                    //oauth2Login.userInfoEndpoint().oidcUserService(OidcUserService())
                    oauth2Login.redirectionEndpoint().baseUri("/callback")
                }
    }
}
