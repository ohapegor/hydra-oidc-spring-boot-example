package ru.ohapegor.oid.consent.hydraconsent.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(user: User) : UserDetails {

    private val authorities: List<GrantedAuthority> = user.authorities.map(::SimpleGrantedAuthority)
    private val password = user.password
    private val userName = user.id
    private val enabled = user.enabled

    override fun getAuthorities() = authorities

    override fun getPassword() = password

    override fun getUsername() = userName

    override fun isAccountNonExpired() = enabled

    override fun isAccountNonLocked() = !enabled

    override fun isCredentialsNonExpired() = !enabled

    override fun isEnabled() = enabled
}