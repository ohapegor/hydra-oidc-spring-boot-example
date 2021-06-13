package ru.ohapegor.oid.consent.hydraconsent.service

import org.springframework.security.core.context.SecurityContextHolder
import ru.ohapegor.oid.consent.hydraconsent.model.User

object SecurityUtils {
    fun currentUser(): User = mayBeCurrentUser() ?: error("not authenticated")
    fun mayBeCurrentUser(): User? = SecurityContextHolder.getContext()?.authentication?.principal?.let { it as User }
}