package ru.ohapegor.oid.consent.hydraconsent.client.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class HydraLoginRequest(
        val challenge: String,
        val client: HydraClient,
        //val oidc_context: OidcContext,
        val requestUrl: String,
        val requestedAccessTokenAudience: List<Any>,
        val requestedScope: List<String>,
        val sessionId: String,
        val skip: Boolean,
        val subject: String,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class HydraConsentRequest(
        val challenge: String,
        val client: HydraClient,
        //val oidc_context: OidcContext,
        val requestUrl: String,
        val requestedAccessTokenAudience: List<String>,
        val requestedScope: List<String>,
        val sessionId: String?,
        val skip: Boolean,
        val subject: String,
        val loginSessionId: String
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class HydraClient(
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
data class HydraAcceptLoginRequest(
        val acr: String? = null,
        //val context: Context? = null,
        val forceSubjectIdentifier: String? = null,
        val remember: Boolean = true,
        val rememberFor: Int? = null,
        val subject: String
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class HydraAcceptConsentRequest(
        val remember: Boolean = true,
        //session expire in seconds, 0 - means never expire
        val rememberFor: Int? = null,
        // val handedAt: Instant = Instant.now(),
        val grantAccessTokenAudience: List<String>,
        val grantScope: List<String>,
        val session: HydraSession,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class HydraSession(
        val idToken: HydraIdToken? = null,
        val access_token: Map<String, String>? = null,
)


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class HydraCommonRedirectResponse(
        val redirectTo: String
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class HydraIdToken(
        val subject: String,
        val address: HydraAddress? = null,
        val birthdate: String? = null,
        // val claims: Claims,
        val email: String? = null,
        val emailVerified: String? = null,
        val familyName: String? = null,
        val fullName: String? = null,
        val gender: String? = null,
        val givenName: String? = null,
        val locale: String? = null,
        val middleName: String? = null,
        val nickName: String? = null,
        val phoneNumber: String? = null,
        val phoneNumberVerified: String? = null,
        val picture: String? = null,
        val preferredUsername: String? = null,
        val profile: String? = null,
        val updatedAt: String? = null,
        val website: String? = null,
        val zoneInfo: String? = null
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class HydraAddress(
        val country: String? = null,
        val formatted: String? = null,
        val locality: String? = null,
        val postalCode: String? = null,
        val region: String? = null,
        val streetAddress: String? = null
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class HydraRejectRequest(
        val error: String? = null,
        val errorDebug: String? = null,
        val errorDescription: String? = null,
        val errorHint: String? = null,
        val statusCode: Int? = null
)