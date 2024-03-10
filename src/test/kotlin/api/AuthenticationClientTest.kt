package api

import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class AuthenticationClientTest {

    @Test
    fun clientCredentialsGrantFlow() {
        val credential = runBlocking { AuthenticationClient.clientCredentialsGrantFlow() }
        println("Your App Credential is: $credential")
    }
}