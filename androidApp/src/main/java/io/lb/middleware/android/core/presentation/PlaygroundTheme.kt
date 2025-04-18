package io.lb.middleware.android.core.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object PlaygroundColors {
    val DarkBlue = 0xFF030826
    val DarkGrayBlue = 0xFF18163A
    val PrimaryPink = 0xFFF544B1
    val PrimaryDarkPink = 0xFFCB0686
    val BackgroundLightBlue = 0xFFD1D7F3
    val SurfaceBlue = 0xFFBDC6F3
    val SecondaryPurple = 0xFF4F00CE
    val TertiaryYellow = 0xFFFFC107
    val ButtonGreen = 0xFF00C853
}

@Composable
fun PlaygroundTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            background = Color(PlaygroundColors.DarkBlue),
            surface = Color(PlaygroundColors.DarkGrayBlue),
            onSurface = Color(PlaygroundColors.PrimaryPink),
            primary = Color(PlaygroundColors.PrimaryPink),
            onPrimary = Color(PlaygroundColors.DarkGrayBlue),
            secondary = Color(PlaygroundColors.SecondaryPurple),
            onSecondary = Color(PlaygroundColors.PrimaryPink),
            tertiary = Color(PlaygroundColors.TertiaryYellow),
            onTertiary = Color(PlaygroundColors.DarkGrayBlue),
        )
    } else {
        lightColorScheme(
            background = Color(PlaygroundColors.BackgroundLightBlue),
            surface = Color(PlaygroundColors.SurfaceBlue),
            onSurface = Color(PlaygroundColors.PrimaryDarkPink),
            primary = Color(PlaygroundColors.PrimaryDarkPink),
            onPrimary = Color.White,
            secondary = Color(PlaygroundColors.SecondaryPurple),
            onSecondary = Color(PlaygroundColors.PrimaryPink),
            tertiary = Color(PlaygroundColors.TertiaryYellow),
            onTertiary = Color(PlaygroundColors.DarkGrayBlue),
        )
    }
    val typography = Typography(
        bodyMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
    )
    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
