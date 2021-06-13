package ru.ohapegor.oid.consent.hydraconsent.controller.dto

data class ConsentRequest(
        val consentChallenge: String,
        val remember: Boolean,
        val grantScope: List<String>
)