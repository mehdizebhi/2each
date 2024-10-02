package config

import com.github.twitch4j.TwitchClient
import com.github.twitch4j.TwitchClientBuilder
import util.AppProperties

object ClientConfig {

    private val clientId: String = AppProperties.getProperty("twitch.app.clientId")
    private val clientSecret: String = AppProperties.getProperty("twitch.app.clientSecret")

    fun twitchClient(): TwitchClient {
        return TwitchClientBuilder.builder()
            .withClientId(clientId)
            .withClientSecret(clientSecret)
            .withEnableHelix(true)
            .build()
    }
}