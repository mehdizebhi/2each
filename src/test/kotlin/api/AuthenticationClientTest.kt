package api

import kotlinx.coroutines.runBlocking
import util.CryptoUtils
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.Test

class AuthenticationClientTest {

    @Test
    fun clientCredentialsGrantFlow() {
        val credential = runBlocking { AuthenticationClient.clientCredentialsGrantFlow() }
        println("Your App Credential is: $credential")
    }

    @Test
    fun deviceCodeGrantFlow() {
        val code = runBlocking { AuthenticationClient.deviceCodeGrantFlow() }
        println("Your Device Code is: $code")
    }

    @Test
    fun userAccessTokenFlow() {
        val token = runBlocking { AuthenticationClient.fetchOAuthTokenByDeviceCode("deviceCode") }

        val encryptedAccessToken = CryptoUtils.encrypt(token.accessToken)
        val encryptedRefreshToken = CryptoUtils.encrypt(token.accessToken)
        val tokenPairString = "$encryptedAccessToken\n$encryptedRefreshToken"
        Files.write(Path.of("tokenpairTest.txt"), tokenPairString.toByteArray())
    }
}