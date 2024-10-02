package config

import api.AuthenticationClient
import internal.OAuthService
import kotlinx.coroutines.runBlocking
import util.AppProperties

object AppContext {

    private val baseUrl: String = AppProperties.getProperty("twitch.api.baseUrl")
    private val clientId: String = AppProperties.getProperty("twitch.app.clientId")
    private val clientSecret: String = AppProperties.getProperty("twitch.app.clientSecret")
    private val redirectUri: String = AppProperties.getProperty("twitch.oauth.redirectUri")
    private val scope: String = AppProperties.getProperty("twitch.oauth.scope")
    private val authenticationClient: AuthenticationClient by lazy {AuthenticationClient(baseUrl, clientId, clientSecret, redirectUri, scope)}

    fun init() {
        val oAuthService = OAuthService(authenticationClient)

        /*runBlocking {
            oAuthService.authenticate()
        }*/
    }

    fun destroy() {
        authenticationClient.close()
    }

    // -------------------------------
    // private helper
    // -------------------------------
}