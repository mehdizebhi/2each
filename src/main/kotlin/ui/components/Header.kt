package ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.github.twitch4j.helix.domain.User

@Composable
fun Header(
    onLogout: () -> Unit,
    onSettings: () -> Unit,
    onWhispers: () -> Unit,
    onNotifications: () -> Unit,
    user: User?
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.secondaryVariant,
        contentPadding = PaddingValues(horizontal = 16.dp),
        elevation = 4.dp
    ) {
        // Left: Twitch Icon
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Distributes elements across the row
        ) {
            // Left side (Twitch Icon)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource("static/images/twitch.svg"),
                    contentDescription = "Twitch Icon",
                    modifier = Modifier.size(40.dp)
                )
            }

            // Middle: Search Input Box
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                var query by remember { mutableStateOf("") }
                BasicTextField(
                    value = query,
                    onValueChange = { query = it },
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (query.isEmpty()) {
                            Text("Search", color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
                        }
                        innerTextField()
                    },
                    modifier = Modifier
                        .width(400.dp) // Adjust the width as needed
                        .height(40.dp)
                        .background(MaterialTheme.colors.surface, shape = MaterialTheme.shapes.small)
                        .padding(horizontal = 8.dp)
                        .border(1.dp, MaterialTheme.colors.onSurface, MaterialTheme.shapes.small)
                )
            }

            // Right side (Dropdowns)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End
            ) {
                DropdownMenuComponent(
                    iconPath = "static/icons/inbox.png",
                    onClick = onWhispers,
                    tooltip = "Whispers"
                )
                Spacer(modifier = Modifier.width(8.dp))
                DropdownMenuComponent(
                    iconPath = "static/icons/notification.png",
                    onClick = onNotifications,
                    tooltip = "Notifications"
                )
                Spacer(modifier = Modifier.width(8.dp))
                DropdownMenuComponent(
                    iconPath = "static/icons/user.png",
                    onClick = onLogout,
                    tooltip = "User Menu",
                    dropdownItems = listOf(
                        DropdownItem("Settings", onSettings),
                        DropdownItem("Logout", onLogout)
                    )
                )
            }
        }
    }
}

@Composable
fun DropdownMenuComponent(
    iconPath: String,
    onClick: () -> Unit,
    tooltip: String,
    dropdownItems: List<DropdownItem> = emptyList()
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Image(
            painter = painterResource(iconPath),
            contentDescription = tooltip,
            modifier = Modifier
                .size(30.dp)
                .padding(4.dp)
                .clip(CircleShape)
                .clickable { expanded = true }
        )

        // Tooltip
        Tooltip(text = tooltip)

        // Dropdown Menu
        if (expanded && dropdownItems.isNotEmpty()) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                dropdownItems.forEach { item ->
                    DropdownMenuItem(onClick = {
                        expanded = false
                        item.onClick()
                    }) {
                        Text(item.title)
                    }
                }
            }
        }
    }
}

data class DropdownItem(val title: String, val onClick: () -> Unit)

@Composable
fun Tooltip(text: String) {
    // Implement tooltip logic if needed
    // For simplicity, not implemented here
}

@Preview
@Composable
fun PreviewHeader() {
    Header(
        onLogout = { println("Logout clicked") },
        onSettings = { println("Settings clicked") },
        onWhispers = { println("Whispers clicked") },
        onNotifications = { println("Notifications clicked") },
        null
    )
}
