package ru.ohapegor.oid.consent.hydraconsent.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.ohapegor.oid.consent.hydraconsent.model.User
import java.util.Optional

interface UsersRepository : JpaRepository<User,String> {

    fun findByEmail(email: String) : Optional<User>
}