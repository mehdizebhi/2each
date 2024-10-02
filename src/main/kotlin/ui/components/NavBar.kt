package ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.dp
import model.LiveStream

@Composable
fun LeftSideBar(liveStreams: List<LiveStream>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .width(240.dp)  // Increased width to accommodate more information
            .background(MaterialTheme.colors.secondary)
            .padding(vertical = 8.dp)
    ) {
        items(liveStreams.size) { index ->
            NavBarItem(stream = liveStreams[index])
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun NavBarItem(stream: LiveStream) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()  // Adjust height to fit content
            .background(MaterialTheme.colors.surface)
            .padding(8.dp)
            .clickable { /* Handle click if needed */ },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Stream logo
        AsyncImage(
            load  = { loadImageBitmap(stream.logoPath) },
            contentDescription = "${stream.username}'s logo",
            painterFor = { remember { BitmapPainter(it) } },
            modifier = Modifier
                .size(42.dp)
                .padding(5.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(2.dp))

        // Stream details (Username, Game, Title, Views)
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 1.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${stream.username} • ${stream.game}",
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.padding(vertical = 1.dp)
            )
            Text(
                text = stream.title.take(20) + if (stream.title.length > 20) "..." else "",
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(vertical = 1.dp)

            )
            Text(
                text = "${stream.type} | ${formatViewersCount(stream.views)} viewers",
                style = MaterialTheme.typography.overline,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(vertical = 1.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewNavBar() {
    val sampleStreams = listOf(
        LiveStream("Streamer1", "Game1", "This is a sample stream title that is quite long", 1000, "live", "static/images/twitch.svg"),
        LiveStream("Streamer2", "Game2", "Another stream title", 2000,"live", "static/images/twitch.svg")
    )
    LeftSideBar(liveStreams = sampleStreams)
}

private fun formatViewersCount(views: Int): String {
    return when {
        views >= 1_000_000 -> String.format("%.1fM", views / 1_000_000.0)
        views >= 1_000 -> String.format("%.1fK", views / 1_000.0)
        else -> views.toString()
    }
}