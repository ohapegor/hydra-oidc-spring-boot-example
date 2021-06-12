package ru.ohapegor.oid.consent.hydraconsent.config

import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


@EnableWebSecurity
class SecurityConfig(
        private val userDetailsService: UserDetailsService
) : WebSecurityConfigurerAdapter() {


    override fun configure(http: HttpSecurity) {
        http
                .csrf().disable()
                .authorizeRequests { authorizeRequests ->
                    authorizeRequests
                            .antMatchers(HttpMethod.GET, "/login").permitAll()
                            .antMatchers(HttpMethod.POST, "/login").permitAll()
                            .antMatchers("/free","/h2-console").permitAll()
                            .anyRequest().permitAll()
                }
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        val passwordEncoder = passwordEncoder()
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
    }

    @Bean
    override fun authenticationManager(): AuthenticationManager {
        return super.authenticationManager()
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

}