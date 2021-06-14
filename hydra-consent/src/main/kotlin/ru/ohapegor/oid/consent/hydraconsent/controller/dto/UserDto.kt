package ru.ohapegor.oid.consent.hydraconsent.controller.dto

data class UserDto(
        val sub: String,
        val email: String? = null
)