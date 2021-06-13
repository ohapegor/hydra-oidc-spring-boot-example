package ru.ohapegor.oid.consent.hydraconsent.model

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID
import javax.persistence.CollectionTable
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.Table

@Table(name = "users")
@Entity
data class User(
        @Id
        val id: String = UUID.randomUUID().toString(),
        @Column(unique = true)
        val email: String = "",
        private val password: String = "",
        val firstname: String = "",
        val lastname: String = "",
        val address: String? = null,
        val enabled: Boolean = true,
        @ElementCollection(fetch = FetchType.EAGER)
        @CollectionTable(
                name = "roles",
                joinColumns = [JoinColumn(name = "USER_ID")]
        )
        val roles: List<String> = emptyList()
) : UserDetails {

    override fun getAuthorities() = roles.map(::SimpleGrantedAuthority)

    override fun getPassword() = password

    override fun getUsername() = id

    override fun isAccountNonExpired() = enabled

    override fun isAccountNonLocked() = enabled

    override fun isCredentialsNonExpired() = enabled

    override fun isEnabled() = enabled

}
