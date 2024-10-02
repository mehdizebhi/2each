package api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import model.*

class AuthenticationClient(
    private val baseUrl: String,
    private val clientId: String,
    private val clientSecret: String,
    private val redirectUri: String,
    private val scope: String
) {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    val authorizationUrl = "$baseUrl/oauth2/authorize" +
            "?client_id=$clientId" +
            "&redirect_uri=$redirectUri" +
            "&response_type=code" +
            "&scope=$scope"

    suspend fun clientCredentialsGrantFlow(): ClientCredential {
        val response: ClientCredential = client.submitForm(
            url = "$baseUrl/oauth2/token",
            formParameters = parameters {
                append("client_id", clientId)
                append("client_secret", clientSecret)
                append("grant_type", "client_credentials")
            }
        ).body()

        return response
    }

    suspend fun deviceCodeGrantFlow(): DeviceCredential {
        val response: DeviceCredential = client.submitForm(
            url = "$baseUrl/oauth2/device",
            formParameters = parameters {
                append("client_id", clientId)
                append("scopes", "user:read:broadcast")
            }
        ).body()

        return response
    }

    suspend fun fetchOAuthTokenByDeviceCode(deviceCode: String): AuthenticationToken {
        val response: AuthenticationToken = client.submitForm(
            url = "$baseUrl/oauth2/token",
            formParameters = parameters {
                append("client_id", clientId)
                append("scopes", "user:read:broadcast")
                append("device_code", deviceCode)
                append("grant_type", "urn:ietf:params:oauth:grant-type:device_code")
            }
        ).body()

        return response
    }

    suspend fun fetchRefreshAccessToken(refreshToken: String): RefreshAuthenticationToken {
        val response: RefreshAuthenticationToken = client.post("$baseUrl/oauth2/token") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody("grant_type=refresh_token&refresh_token=$refreshToken&client_id=$clientId&client_secret=$clientSecret")
        }.body()

        return response
    }

    suspend fun exchangeCodeForToken(code: String): TokenResponse {
        val response: TokenResponse = client.post("$baseUrl/oauth2/token") {
            parameter("client_id", clientId)
            parameter("client_secret", clientSecret)
            parameter("code", code)
            parameter("grant_type", "authorization_code")
            parameter("redirect_uri", redirectUri)
        }.body()

        return response
    }

    suspend fun refreshToken(refreshToken: String): TokenResponse {
        val response: TokenResponse = client.post("$baseUrl/oauth2/token") {
            parameter("grant_type", "refresh_token")
            parameter("refresh_token", refreshToken)
            parameter("client_id", clientId)
            parameter("client_secret", clientSecret)
        }.body()

        return response
    }

    fun close() {
        client.close()
    }
}