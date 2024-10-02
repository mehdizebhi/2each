package api

import com.github.twitch4j.TwitchClient
import com.github.twitch4j.helix.domain.User
import model.LiveStream

object TwitchApi {

    fun loggedInUser(client: TwitchClient, accessToken: String): User {
        val result = client.helix.getUsers(accessToken, null, null).execute()
        return result.users[0]
    }

    fun userWithId(client: TwitchClient, userId: String): User {
        val result = client.helix.getUsers(null, listOf(userId), null).execute()
        return result.users[0]
    }

    fun liveFollowedStreams(client: TwitchClient, accessToken: String, user: User): List<LiveStream> {
        val result = client.helix.getFollowedStreams(accessToken, user.id, null, 10).execute()
        return result.streams.map { stream -> LiveStream(
            username = stream.userName,
            game = stream.gameName,
            title = stream.title,
            views = stream.viewerCount,
            logoPath = userWithId(client, stream.userId).profileImageUrl,
            type = stream.type
        ) }
    }
}