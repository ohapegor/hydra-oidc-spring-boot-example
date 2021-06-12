package ru.ohapegor.oid.consent.hydraconsent.config

import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.sql.DataSource


@EnableWebSecurity
class SecurityConfig(
        private val dataSource: DataSource
) : WebSecurityConfigurerAdapter() {


    override fun configure(http: HttpSecurity) {
        http
                .csrf().disable()
                // .userDetailsService(authenticationManagerBean())
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
        auth//.userDetailsService(userDetailsService)
                .jdbcAuthentication()
                .dataSource(dataSource)
                .withDefaultSchema()
                .passwordEncoder(passwordEncoder)
                .withUser(User
                        .withUsername("foo@bar.com")
                        .password(passwordEncoder.encode("foobar"))
                        .roles("USER")
                )
    }

    @Bean
    override fun userDetailsService(): UserDetailsService {
        return super.userDetailsService()
    }

    @Bean
    override fun authenticationManager(): AuthenticationManager {
        return super.authenticationManager()
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

}