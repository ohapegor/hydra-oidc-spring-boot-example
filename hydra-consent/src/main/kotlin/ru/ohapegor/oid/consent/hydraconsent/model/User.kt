package ru.ohapegor.oid.consent.hydraconsent.model

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
        val password: String = "",
        val firstname: String = "",
        val lastname: String = "",
        val address: String? = null,
        val enabled: Boolean = true,
        @ElementCollection(fetch = FetchType.EAGER)
        @CollectionTable(
                name = "authorities",
                joinColumns = [JoinColumn(name = "USER_ID")]
        )
        val authorities: List<String> = emptyList()
)
