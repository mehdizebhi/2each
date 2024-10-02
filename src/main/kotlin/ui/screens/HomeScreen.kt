package ui.screens

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.twitch4j.helix.domain.User
import model.LiveStream
import model.SuggestedStream
import ui.components.*

@Composable
fun HomeScreen(
    liveStreams: List<LiveStream>,
    suggestedStreams: List<SuggestedStream>,
    followedStreams: List<SuggestedStream>,
    onLogout: () -> Unit,
    onSettings: () -> Unit,
    onWhispers: () -> Unit,
    onNotifications: () -> Unit,
    user: User?
) {
    Column {
        // Header
        Header(
            onLogout = onLogout,
            onSettings = onSettings,
            onWhispers = onWhispers,
            onNotifications = onNotifications,
            user = user
        )

        Row(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
            // Left: NavBar
            LeftSideBar(liveStreams = liveStreams)

            // Middle: Suggested Streams and Followed Channels
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = "Suggested Streams",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colors.onBackground
                )
                SuggestedStreamsList(suggestedStreams = suggestedStreams)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Followed Channels",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colors.onBackground
                )
                SuggestedStreamsList(suggestedStreams = followedStreams)
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    val liveStreams = listOf(
        LiveStream("Streamer1", "Game1", "Live Stream 1", 1000, "live","static/images/twitch.svg"),
        LiveStream("Streamer2", "Game2", "Live Stream 2", 2000, "live","static/images/twitch.svg"),

        // Add more sample live streams
    )

    val suggestedStreams = listOf(
        SuggestedStream("Streamer1", "Sample Stream Title 1", 1500, "static/images/twitch.svg"),
        SuggestedStream("Streamer2", "Sample Stream Title 2", 2500, "static/images/twitch.svg"),
        SuggestedStream("Streamer3", "Sample Stream Title 3", 3500, "static/images/twitch.svg"),
        // Add more sample suggested streams
    )

    val followedStreams = listOf(
        SuggestedStream("StreamerA", "Followed Stream 1", 5000, "static/images/twitch.svg"),
        SuggestedStream("StreamerB", "Followed Stream 2", 6000, "static/images/twitch.svg"),
        // Add more sample followed streams
    )

    HomeScreen(
        liveStreams = liveStreams,
        suggestedStreams = suggestedStreams,
        followedStreams = followedStreams,
        onLogout = { println("Logout clicked") },
        onSettings = { println("Settings clicked") },
        onWhispers = { println("Whispers clicked") },
        onNotifications = { println("Notifications clicked") },
        user = null
    )
}
