package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import model.LiveStream

@Composable
fun Tooltip(stream: LiveStream, show: Boolean) {
    if (show) {
        Popup(alignment = Alignment.Center) {
            Card(
                backgroundColor = Color(0xCC000000),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = "Username: ${stream.username}", color = Color.White)
                    Text(text = "Game: ${stream.game}", color = Color.White)
                    Text(
                        text = "Title: ${stream.title.take(30)}...",
                        color = Color.White
                    )
                    Text(text = "Views: ${stream.views}", color = Color.White)
                }
            }
        }
    }
}