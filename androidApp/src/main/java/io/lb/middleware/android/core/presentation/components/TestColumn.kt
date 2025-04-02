package io.lb.middleware.android.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.lb.middleware.android.core.presentation.PlaygroundColors
import io.lb.middleware.android.core.presentation.showToast

@Composable
fun TestColumn(
    isLoading: Boolean,
    result: String,
    idleText: String? = null,
    progressText: String? = null,
    onClick: () -> Unit,
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier.padding(top = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DefaultTextButton(
            modifier = Modifier.fillMaxWidth(0.7f),
            enabled = isLoading.not(),
            text = if (isLoading) {
                progressText ?: "Testing Route"
            } else {
                idleText ?: "Test Route"
            },
            imageVector = Icons.Default.PlayArrow,
            containerColor = Color(PlaygroundColors.ButtonGreen),
            contentColor = Color.White
        ) {
            onClick.invoke()
        }
        Spacer(modifier = Modifier.padding(12.dp))

        if (isLoading) {
            CircularProgressIndicator()
        }

        AnimatedVisibility(
            result.isNotEmpty()
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Result",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(
                        onClick = {
                            clipboardManager.setText(
                                buildAnnotatedString {
                                    append(result)
                                }
                            )
                            context.showToast("Copied to clipboard")
                        }
                    ) {
                        Icon(
                            modifier = Modifier,
                            imageVector = Icons.Filled.ContentCopy,
                            contentDescription = "Copy path"
                        )
                    }
                }
                Text(
                    text = result,
                    fontSize = 16.sp,
                )
                Spacer(modifier = Modifier.padding(12.dp))
            }
        }
    }
}
