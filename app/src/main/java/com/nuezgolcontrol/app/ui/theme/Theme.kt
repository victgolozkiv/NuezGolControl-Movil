package com.nuezgolcontrol.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val PurplePrimary = Color(0xFF6F42C1)
val PurpleText = Color(0xFFCE93D8)
val PurpleDeep = Color(0xFF4A148C)
val TealTotal = Color(0xFF20C997)
val SurfaceDark = Color(0xFF121212)
val SurfaceElevated = Color(0xFF1A1A1A)
val BorderMuted = Color(0xFF444444)
val Danger = Color(0xFFE57373)

private val NuezDarkColors = darkColorScheme(
    primary = PurplePrimary,
    onPrimary = Color.White,
    secondary = TealTotal,
    onSecondary = Color.Black,
    background = Color.Black,
    onBackground = PurpleText,
    surface = SurfaceElevated,
    onSurface = PurpleText,
    surfaceVariant = SurfaceDark,
    onSurfaceVariant = PurpleText,
    error = Danger,
    onError = Color.White,
    outline = BorderMuted
)

@Composable
fun NuezGolTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NuezDarkColors,
        typography = MaterialTheme.typography.copy(
            headlineMedium = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = PurpleText
            ),
            titleLarge = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = PurpleText
            ),
            bodyLarge = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 16.sp,
                color = PurpleText
            ),
            bodyMedium = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontSize = 14.sp,
                color = PurpleText
            ),
            labelLarge = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        ),
        content = content
    )
}
