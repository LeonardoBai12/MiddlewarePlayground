package io.lb.middleware.android.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import io.lb.middleware.common.shared.util.beautifyJson

@Composable
fun RouteDetails(
    originalQueries: Map<String, String>?,
    originalHeaders: Map<String, String>?,
    originalBody: String?,
    preConfiguredQueries: Map<String, String>?,
    preConfiguredHeaders: Map<String, String>?,
    preConfiguredBody: String?,
) {
    val showOriginalHeaders = remember { mutableStateOf(false) }
    val showOriginalQueries = remember { mutableStateOf(false) }
    val showOriginalBody = remember { mutableStateOf(false) }
    val showPreConfiguredHeaders = remember { mutableStateOf(false) }
    val showPreConfiguredQueries = remember { mutableStateOf(false) }
    val showPreConfiguredBody = remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        originalHeaders?.takeIf { it.isNotEmpty() }?.let { headers ->
            ToggleSection(
                title = "Original Headers",
                showSection = showOriginalHeaders,
                content = {
                    headers.forEach { (key, value) ->
                        Text(
                            text = "$key: $value",
                            fontSize = 16.sp,
                        )
                    }
                }
            )
        }

        originalQueries?.takeIf { it.isNotEmpty() }?.let { queries ->
            ToggleSection(
                title = "Original Queries",
                showSection = showOriginalQueries,
                content = {
                    queries.forEach { (key, value) ->
                        Text(
                            text = "$key: $value",
                            fontSize = 16.sp,
                        )
                    }
                }
            )
        }

        originalBody?.takeIf {
            it.isNotEmpty() && it != "{}" && it != "null"
        }?.let { body ->
            ToggleSection(
                title = "Original Body",
                showSection = showOriginalBody,
                content = {
                    Text(
                        text = beautifyJson(body),
                        fontSize = 16.sp,
                    )
                }
            )
        }

        preConfiguredHeaders?.takeIf { it.isNotEmpty() }?.let { headers ->
            ToggleSection(
                title = "Pre-configured Headers",
                showSection = showPreConfiguredHeaders,
                content = {
                    headers.forEach { (key, value) ->
                        Text(
                            text = "$key: $value",
                            fontSize = 16.sp,
                        )
                    }
                }
            )
        }

        preConfiguredQueries?.takeIf { it.isNotEmpty() }?.let { queries ->
            ToggleSection(
                title = "Pre-configured Queries",
                showSection = showPreConfiguredQueries,
                content = {
                    queries.forEach { (key, value) ->
                        Text(
                            text = "$key: $value",
                            fontSize = 16.sp,
                        )
                    }
                }
            )
        }

        preConfiguredBody?.takeIf {
            it.isNotEmpty() && it != "{}" && it != "null"
        }?.let { body ->
            ToggleSection(
                title = "Pre-configured Body",
                showSection = showPreConfiguredBody,
                content = {
                    Text(
                        text = beautifyJson(body),
                        fontSize = 16.sp,
                    )
                }
            )
        }
    }
}

@Composable
private fun ToggleSection(
    title: String,
    showSection: MutableState<Boolean>,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .clickable { showSection.value = !showSection.value },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
            )
            IconButton(
                onClick = { showSection.value = !showSection.value }
            ) {
                Icon(
                    imageVector = if (showSection.value) {
                        Icons.Default.ArrowDropDown
                    } else {
                        Icons.Default.ArrowRight
                    },
                    contentDescription = "Toggle $title"
                )
            }
        }

        AnimatedVisibility(visible = showSection.value) {
            Column {
                content()
            }
        }
    }
}
