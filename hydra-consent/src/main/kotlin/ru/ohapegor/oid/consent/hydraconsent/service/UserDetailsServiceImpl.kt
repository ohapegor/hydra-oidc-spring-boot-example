package ru.ohapegor.oid.consent.hydraconsent.service

import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import ru.ohapegor.oid.consent.hydraconsent.model.User
import ru.ohapegor.oid.consent.hydraconsent.repository.UsersRepository

@Service
class UserDetailsServiceImpl(
        private val usersRepository: UsersRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): User? {
        return usersRepository.findByEmail(username).orElse(null)
    }
}