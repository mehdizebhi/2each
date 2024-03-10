package api

import model.ClientCredential
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
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


}