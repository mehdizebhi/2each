package internal

import api.AuthenticationClient
import kotlinx.coroutines.runBlocking
import model.StoredToken
import model.TokenResponse
import java.awt.Desktop
import java.net.URI

class OAuthService(private val client: AuthenticationClient) {

    suspend fun authenticate(): TokenResponse {
        val server = OAuthServer()
        server.start()

        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(URI(client.authorizationUrl))
        } else {
            println("Please open the following URL in your browser:")
            println(client.authorizationUrl)
        }

        val code = server.waitForCode()

        val tokenResponse = client.exchangeCodeForToken(code)

        server.stop()

        val storedToken = StoredToken(
            accessToken = tokenResponse.accessToken,
            refreshToken = tokenResponse.refreshToken,
            expiresAt = System.currentTimeMillis() + tokenResponse.expiresIn * 1000
        )
        TokenStorage.saveToken(storedToken)

        return tokenResponse
    }

    suspend fun accessToken(): String {
        val storedToken = TokenStorage.loadToken() ?: throw Exception("Not authenticated")
        return if (System.currentTimeMillis() > storedToken.expiresAt) {
            val refreshed = client.refreshToken(storedToken.refreshToken)
            StoredToken(
                accessToken = refreshed.accessToken,
                refreshToken = refreshed.refreshToken,
                expiresAt = System.currentTimeMillis() + refreshed.expiresIn * 1000
            ).also { TokenStorage.saveToken(it) }.accessToken
        } else {
            storedToken.accessToken
        }
    }

    fun logout() {
        TokenStorage.deleteToken()
    }

    fun isAuthenticated(): Boolean {
        return runBlocking {
            try {
                // Try to get the access token; if successful, user is authenticated
                accessToken()
                true
            } catch (e: Exception) {
                // If an exception occurs, user is not authenticated
                false
            }
        }
    }
}