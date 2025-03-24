@file:OptIn(ExperimentalMaterial3Api::class)

package io.lb.middleware.android.routes.details.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import io.lb.middleware.android.core.presentation.PlaygroundColors
import io.lb.middleware.android.core.presentation.Screens
import io.lb.middleware.android.core.presentation.components.DefaultTextButton
import io.lb.middleware.android.core.presentation.components.GenericTopAppBar
import io.lb.middleware.android.core.presentation.components.MethodBox
import io.lb.middleware.android.core.presentation.showToast
import io.lb.middleware.common.shared.middleware.model.MappedRoute
import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.shared.presentation.middleware.details.RouteDetailsEvent
import io.lb.middleware.shared.presentation.middleware.details.RouteDetailsState
import io.lb.middleware.shared.presentation.middleware.details.RouteDetailsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

@Composable
fun RouteDetailsScreen(
    route: MappedRoute?,
    navController: NavHostController,
    state: RouteDetailsState,
    eventFlow: CommonFlow<RouteDetailsViewModel.UiEvent>,
    onEvent: (RouteDetailsEvent) -> Unit,
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val result = remember {
        mutableStateOf("")
    }

    route ?: return

    LaunchedEffect(key1 = Screens.ROUTE_DETAILS) {
        eventFlow.collectLatest {
            when (it) {
                is RouteDetailsViewModel.UiEvent.ShowError -> {
                    context.showToast(it.message)
                }
                is RouteDetailsViewModel.UiEvent.ShowResult -> {
                    result.value = it.result
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        onEvent(RouteDetailsEvent.SaveRouteInHistory(route ?: return@LaunchedEffect))
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            GenericTopAppBar(navController)
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Original Base URL",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = route?.originalBaseUrl?.substringAfterLast("://") ?: "",
                        fontSize = 16.sp,
                    )
                    Spacer(modifier = Modifier.padding(12.dp))
                }
            }
            item {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Original Path",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = route?.originalPath?.substringAfterLast("://") ?: "",
                        fontSize = 16.sp,
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }

            item {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Mapped Path",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        IconButton(
                            onClick = {
                                clipboardManager.setText(
                                    buildAnnotatedString {
                                        append(route?.path)
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
                        modifier = Modifier.fillMaxWidth(),
                        text = route?.path?.substringAfterLast("://") ?: "",
                        fontSize = 16.sp,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.padding(12.dp))
                }
            }
            item {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        MethodBox(route.originalMethod, "Original Methods")
                        Spacer(modifier = Modifier.padding(16.dp))
                        MethodBox(route.method, "Mapped Methods")
                    }
                    Spacer(modifier = Modifier.padding(12.dp))
                }
            }
            item {
                RouteDetails(route)
                Spacer(modifier = Modifier.padding(12.dp))
            }

            item {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    DefaultTextButton(
                        modifier = Modifier.fillMaxWidth(0.7f),
                        enabled = state.isLoading.not(),
                        text = if (state.isLoading) {
                            "Testing Route"
                        } else {
                            "Test Route"
                        },
                        imageVector = Icons.Default.PlayArrow,
                        containerColor = Color(PlaygroundColors.ButtonGreen),
                        contentColor = Color.White
                    ) {
                        result.value = ""
                        onEvent(RouteDetailsEvent.TestRoute)
                    }
                    Spacer(modifier = Modifier.padding(12.dp))

                    if (state.isLoading) {
                        CircularProgressIndicator()
                    }

                    AnimatedVisibility(
                        result.value.isNotEmpty()
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
                                                append(result.value)
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
                                text = result.value,
                                fontSize = 16.sp,
                            )
                            Spacer(modifier = Modifier.padding(12.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RouteDetails(route: MappedRoute?) {
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
        route?.originalHeaders?.takeIf { it.isNotEmpty() }?.let { headers ->
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

        route?.originalQueries?.takeIf { it.isNotEmpty() }?.let { queries ->
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

        route?.originalBody?.takeIf {
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

        route?.preConfiguredHeaders?.takeIf { it.isNotEmpty() }?.let { headers ->
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

        route?.preConfiguredQueries?.takeIf { it.isNotEmpty() }?.let { queries ->
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

        route?.preConfiguredBody?.takeIf {
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

private fun beautifyJson(jsonString: String): String {
    val json = Json {
        prettyPrint = true
    }
    val jsonObject = json.parseToJsonElement(jsonString).jsonObject
    return json.encodeToString(JsonObject.serializer(), jsonObject)
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
