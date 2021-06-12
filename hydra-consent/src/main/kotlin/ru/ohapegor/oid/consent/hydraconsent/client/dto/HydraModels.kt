package ru.ohapegor.oid.consent.hydraconsent.client.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class LoginData(
        val challenge: String,
        val client: Client,
        //val oidc_context: OidcContext,
        val requestUrl: String,
        val requestedAccessTokenAudience: List<Any>,
        val requestedScope: List<String>,
        val sessionId: String,
        val skip: Boolean,
        val subject: String
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Client(
        //val allowed_cors_origins: List<String>,
        //val audience: List<String>,
        val clientId: String,
        val clientName: String,
        val clientSecretExpiresAt: Int,
        val clientUri: String,
        //val contacts: List<Any>,
        val createdAt: String,
        val grantTypes: List<String>,
        //val jwks: Jwks,
        val logoUri: String,
        // val metadata: Metadata,
        val owner: String,
        val policyUri: String,
        val redirectUris: List<String>,
        val responseTypes: List<String>,
        val scope: String,
        val subjectType: String,
        val tokenEndpointAuthMethod: String,
        val tosUri: String,
        val updatedAt: String,
        val userinfoSignedResponseAlg: String
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AcceptLoginRequestBody(
        val acr: String? = null,
        //val context: Context? = null,
        val forceSubjectIdentifier: String? = null,
        val remember: Boolean = true,
        val rememberFor: Int? = null,
        val subject: String
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AcceptLoginResponsetBody(
        val redirectTo: String
)
