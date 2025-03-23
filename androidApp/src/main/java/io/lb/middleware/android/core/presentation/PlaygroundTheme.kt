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

@Composable
fun PlaygroundTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            background = Color(0xFF030826),
            surface = Color(0xFF18163A),
            onSurface = Color(0xFFF544B1),
            primary = Color(0xFFF544B1),
            onPrimary = Color(0xFF030826),
            secondary = Color(0xFFCE3000),
            onSecondary = Color(0xFF030826),
            tertiary = Color(0xFFFFC107),
            onTertiary = Color(0xFF030826),
        )
    } else {
        lightColorScheme(
            primary = Color(0xFFF544B1),
            onPrimary = Color(0xFF030826),
            secondary = Color(0xFFCE3000),
            onSecondary = Color(0xFF030826),
            tertiary = Color(0xFFFFC107),
            onTertiary = Color(0xFF030826),
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
