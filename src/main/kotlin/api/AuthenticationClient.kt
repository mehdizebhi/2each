package api

import exception.AuthorizationPendingException
import exception.InvalidDeviceCodeException
import exception.InvalidRefreshToken
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import model.*
import util.AppProperties

object AuthenticationClient {

    private val baseUrl: String = AppProperties.getProperty("twitch.api.baseUrl")
    private val clientId: String = AppProperties.getProperty("twitch.app.clientId")
    private val clientSecret: String = AppProperties.getProperty("twitch.app.clientSecret")

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun clientCredentialsGrantFlow(): ClientCredential {
        val response: HttpResponse = client.submitForm(
            url = "$baseUrl/oauth2/token",
            formParameters = parameters {
                append("client_id", clientId)
                append("client_secret", clientSecret)
                append("grant_type", "client_credentials")
            }
        )

        if (response.status.value in 200..299) {
            return response.body()
        } else {
            throw RuntimeException("There is a problem to call $baseUrl/oauth2/token. Status code: ${response.status.value}.")
        }
    }

    suspend fun deviceCodeGrantFlow(): DeviceCredential {
        val response: HttpResponse = client.submitForm(
            url = "$baseUrl/oauth2/device",
            formParameters = parameters {
                append("client_id", clientId)
                append("scopes", "user:read:broadcast")
            }
        )

        if (response.status.value in 200..299) {
            return response.body()
        } else {
            throw RuntimeException("There is a problem to call $baseUrl/oauth2/device. Status code: ${response.status.value}.")
        }
    }

    suspend fun fetchOAuthTokenByDeviceCode(deviceCode: String): AuthenticationToken {
        val response: HttpResponse = client.submitForm(
            url = "$baseUrl/oauth2/token",
            formParameters = parameters {
                append("client_id", clientId)
                append("scopes", "user:read:broadcast")
                append("device_code", deviceCode)
                append("grant_type", "urn:ietf:params:oauth:grant-type:device_code")
            }
        )

        when (response.status.value) {
            in 200..299 -> {
                return response.body()
            }
            400 -> {
                val error: ErrorRespose = response.body()
                when (error.message) {
                    "authorization_pending" -> {
                        throw AuthorizationPendingException(error.message)
                    }
                    "invalid device code" -> {
                        throw InvalidDeviceCodeException(error.message)
                    }
                    else -> {
                        throw RuntimeException("Can not fetch OAuth Token")
                    }
                }
            }
            else -> {
                throw RuntimeException("There is a problem to call $baseUrl/oauth2/token. Status code: ${response.status.value}.")
            }
        }
    }

    suspend fun refreshAccessToken(refreshToken: String): RefreshAuthenticationToken {
        val response: HttpResponse = client.post("$baseUrl/oauth2/token") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody("grant_type=refresh_token&refresh_token=$refreshToken&client_id=$clientId&client_secret=$clientSecret")
        }

        when (response.status.value) {
            in 200..299 -> {
                return response.body()
            }
            400 -> {
                throw InvalidRefreshToken("Invalid refresh token")
            }
            else -> {
                throw RuntimeException("There is a problem to call $baseUrl/oauth2/token. Status code: ${response.status.value}.")
            }
        }
    }
}