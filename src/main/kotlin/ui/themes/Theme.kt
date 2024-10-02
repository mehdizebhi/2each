package ui.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Define your own color palettes
private val LightColorPalette = lightColors(
    primary = Color(0xFFA970FF),
    primaryVariant = Color(0xFF3700B3),
    secondary = Color(0xFFEFEFF1),
    secondaryVariant = Color(0xFFFFFFFF),
    background = Color(0xFF0E0E10),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFB00020),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

private val DarkColorPalette = darkColors(
    primary = Color(0xFFA970FF),
    primaryVariant = Color(0xFF3700B3),
    secondary = Color(0xFF1F1F23),
    secondaryVariant = Color(0xFF18181B),
    background = Color(0xFF0E0E10),
    surface = Color(0xFF1E1E1E),
    error = Color(0xFFCF6679),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black
)

@Composable
fun DefaultTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
}