package ru.ohapegor.oid.consent.hydraconsent.controller.dto

data class LoginRequest(
        val loginChallenge: String,
        val email: String,
        val password: String,
        val remember: Boolean
)