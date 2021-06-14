package ru.ohapegor.oidc.hydraclient.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService



@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        val aliOidUserService = OidcUserService()
        aliOidUserService.setAccessibleScopes(mutableSetOf("profile","email"))
        http.authorizeRequests { authorizeRequests ->
            authorizeRequests
                    .anyRequest().authenticated()
        }
                .oauth2Login { oauth2Login ->
                    oauth2Login.userInfoEndpoint().oidcUserService(aliOidUserService)
                    oauth2Login.redirectionEndpoint().baseUri("/callback")
                }
    }
}
