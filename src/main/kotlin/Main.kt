import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import api.AuthenticationClient
import api.TwitchApi
import config.ClientConfig
import internal.OAuthService
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import model.SuggestedStream
import ui.screens.HomeScreen
import ui.screens.LoginScreen
import ui.themes.DefaultTheme
import util.AppProperties
import java.awt.GraphicsEnvironment

@Composable
@Preview
fun App() {

    val baseUrl: String = AppProperties.getProperty("twitch.api.baseUrl")
    val clientId: String = AppProperties.getProperty("twitch.app.clientId")
    val clientSecret: String = AppProperties.getProperty("twitch.app.clientSecret")
    val redirectUri: String = AppProperties.getProperty("twitch.oauth.redirectUri")
    val scope: String = AppProperties.getProperty("twitch.oauth.scope")
    val authenticationClient: AuthenticationClient by lazy { AuthenticationClient(baseUrl, clientId, clientSecret, redirectUri, scope) }
    val oauthService = OAuthService(authenticationClient)

    var isAuthenticated by remember { mutableStateOf(oauthService.isAuthenticated()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    val accessToken = runBlocking {
        oauthService.accessToken()
    }

    val client = ClientConfig.twitchClient()

    fun handleLogin() {
        coroutineScope.launch {
            isLoading = true
            errorMessage = null
            try {
                oauthService.authenticate()
                isAuthenticated = true
                //fetchStreams()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun handleLogout() {
        coroutineScope.launch {
            oauthService.logout()
            isAuthenticated = false
        }
    }

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

    DefaultTheme {
        if (isAuthenticated) {
            val user = TwitchApi.loggedInUser(client, accessToken)
            HomeScreen(
                liveStreams = TwitchApi.liveFollowedStreams(client, accessToken, user),
                suggestedStreams = suggestedStreams,
                followedStreams = followedStreams,
                onLogout = { handleLogout() },
                onSettings = { println("Settings clicked") },
                onWhispers = { println("Whispers clicked") },
                onNotifications = { println("Notifications clicked") },
                user = user
            )
        } else {
            LoginScreen(
                onLoginClick = { handleLogin() },
                isLoading = isLoading
            )
        }

        //
        if (errorMessage != null) {
            Snackbar(
                action = {
                    Button(onClick = { errorMessage = null }) {
                        Text("Dismiss")
                    }
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = errorMessage!!)
            }
        }
    }
}

fun main() = application {
    val screenBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().maximumWindowBounds
    val windowState = rememberWindowState(
        width = screenBounds.width.dp,  // Use full screen width
        height = screenBounds.height.dp  // Use full screen height
    )

    Window(onCloseRequest = ::exitApplication, title = "2each - Twitch Client", state = windowState) {
        App()
    }
}
