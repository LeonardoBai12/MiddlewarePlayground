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
    val background = Color(0xFF030826)
    val surface = Color(0xFF18163A)
    val onSurface = Color(0xFFF544B1)
    val primary = Color(0xFFF544B1)
    val onPrimary = Color(0xFF030826)
    val secondary = Color(0xFFCE3000)
    val onSecondary = Color(0xFF030826)
    val tertiary = Color(0xFFFFC107)
    val onTertiary = Color(0xFF030826)
}

@Composable
fun PlaygroundTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            background = PlaygroundColors.background,
            surface = PlaygroundColors.surface,
            onSurface = PlaygroundColors.onSurface,
            primary = PlaygroundColors.primary,
            onPrimary = PlaygroundColors.onPrimary,
            secondary = PlaygroundColors.secondary,
            onSecondary = PlaygroundColors.onSecondary,
            tertiary = PlaygroundColors.tertiary,
            onTertiary = PlaygroundColors.onTertiary,
        )
    } else {
        lightColorScheme(
            surface = PlaygroundColors.surface,
            onSurface = PlaygroundColors.onSurface,
            primary = PlaygroundColors.primary,
            onPrimary = PlaygroundColors.onPrimary,
            secondary = PlaygroundColors.secondary,
            onSecondary = PlaygroundColors.onSecondary,
            tertiary = PlaygroundColors.tertiary,
            onTertiary = PlaygroundColors.onTertiary,
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
