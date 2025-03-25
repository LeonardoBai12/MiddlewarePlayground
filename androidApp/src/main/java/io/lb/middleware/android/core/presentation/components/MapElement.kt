package io.lb.middleware.android.core.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MapElement(
    key: String = "",
    value: String = "",
    isLoading: Boolean,
    isAdded: Boolean,
    onClickAdd: (String, String) -> Unit,
    onClickRemove: (String) -> Unit
) {
    val mapKey = remember {
        mutableStateOf(key)
    }
    val mapValue = remember {
        mutableStateOf(value)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DefaultTextField(
            modifier = Modifier
                .fillMaxWidth(0.4f),
            text = mapKey.value,
            isEnabled = isLoading.not() && isAdded.not(),
            label = "Key",
            onValueChange = {
                mapKey.value = it
            }
        )
        DefaultTextField(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(0.8f),
            text = mapValue.value,
            isEnabled = isLoading.not() && isAdded.not(),
            label = "Value",
            onValueChange = {
                mapValue.value = it
            }
        )
        Column {
            Spacer(Modifier.height(8.dp))
            if (isAdded) {
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    onClick = {
                        if (mapKey.value.isNotEmpty()) {
                        onClickRemove(mapKey.value)
                            }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Remove Query"
                    )
                }
            } else {
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    onClick = {
                        if (mapKey.value.isNotEmpty() && mapValue.value.isNotEmpty()) {
                            onClickAdd(mapKey.value, mapValue.value)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Query"
                    )
                }
            }
        }
    }
}
