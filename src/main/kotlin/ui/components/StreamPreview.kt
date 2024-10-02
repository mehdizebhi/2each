package ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import model.SuggestedStream

@Composable
fun StreamPreview(stream: SuggestedStream) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { /* Handle stream click */ }
    ) {
        Column {
            Image(
                painter = painterResource(stream.thumbnailPath),
                contentDescription = "${stream.username}'s stream thumbnail",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stream.username,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(
                text = stream.title.take(50) + if (stream.title.length > 50) "..." else "",
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(
                text = "${stream.views} views",
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun SuggestedStreamsList(suggestedStreams: List<SuggestedStream>) {
    LazyRow (
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(suggestedStreams.size) { index ->
            StreamPreview(stream = suggestedStreams[index])
        }
    }
}

@Preview
@Composable
fun PreviewStreamPreview() {
    val sampleStream = SuggestedStream(
        username = "Streamer1",
        title = "This is a sample stream title that is quite long and might need truncation",
        views = 1500,
        thumbnailPath = "thumbnail1.png"
    )
    StreamPreview(stream = sampleStream)
}

@Preview
@Composable
fun PreviewSuggestedStreamsList() {
    val sampleStreams = listOf(
        SuggestedStream("Streamer1", "Title1", 1500, "static/images/twitch.svg"),
        SuggestedStream("Streamer2", "Title2", 2500, "static/images/twitch.svg"),
        SuggestedStream("Streamer3", "Title3", 3500, "static/images/twitch.svg")
    )
    SuggestedStreamsList(suggestedStreams = sampleStreams)
}
