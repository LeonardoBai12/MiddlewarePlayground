package io.lb.middleware.android.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.lb.middleware.android.core.presentation.PlaygroundColors
import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods

@Composable
fun MethodBox(
    method: MiddlewareHttpMethods?,
    text: String? = null,
    modifier: Modifier = Modifier
) {
    Column {
        text?.let {
            Text(
                text = text,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.padding(4.dp))
        }
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(method?.color ?: PlaygroundColors.PrimaryPink))
        ) {
            Text(
                modifier = Modifier.padding(
                    vertical = 4.dp,
                    horizontal = 8.dp
                ).fillMaxWidth(),
                text = method?.name ?: "",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}
