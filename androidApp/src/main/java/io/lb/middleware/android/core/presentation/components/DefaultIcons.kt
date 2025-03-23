package io.lb.middleware.android.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import io.lb.middleware.android.R

@Composable
fun MiddlewareLogoIcon(size: DpSize = DpSize(64.dp, 64.dp)) {
    DefaultIcon(
        modifier = Modifier.size(size),
        painter = painterResource(id = R.mipmap.ic_launcher_foreground),
        shape = RoundedCornerShape(16.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        contentDescription = "LoginHomeScreenIcon",
    )
}

@Composable
fun DefaultIcon(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier.fillMaxSize(),
    shape: Shape = RoundedCornerShape(24.dp),
    containerColor: Color = MaterialTheme.colorScheme.onPrimary,
    contentColor: Color = MaterialTheme.colorScheme.primary,
    painter: Painter,
    contentDescription: String,
) {
    Box(
        modifier = modifier
            .background(
                color = containerColor,
                shape = shape
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = iconModifier,
            painter = painter,
            contentDescription = contentDescription,
            tint = contentColor
        )
    }
}
