package io.lb.middleware.android.createroute.presentation.originalroute

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import io.lb.middleware.android.core.presentation.Screens
import io.lb.middleware.android.core.presentation.components.DefaultTextButton
import io.lb.middleware.android.core.presentation.components.DefaultTextField
import io.lb.middleware.android.core.presentation.components.GenericTopAppBar
import io.lb.middleware.android.core.presentation.components.MapElement
import io.lb.middleware.android.core.presentation.components.MethodBox
import io.lb.middleware.android.core.presentation.components.TestColumn
import io.lb.middleware.android.core.presentation.showToast
import io.lb.middleware.android.core.util.generateOldBodyFieldsFromJson
import io.lb.middleware.android.createroute.presentation.model.AndroidOldBodyField
import io.lb.middleware.android.createroute.presentation.model.CreateRouteArgs
import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.shared.presentation.createroute.originalroute.OriginalRouteEvent
import io.lb.middleware.shared.presentation.createroute.originalroute.OriginalRouteState
import io.lb.middleware.shared.presentation.createroute.originalroute.OriginalRouteViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OriginalRouteScreen(
    navController: NavHostController,
    state: OriginalRouteState,
    eventFlow: CommonFlow<OriginalRouteViewModel.UiEvent>,
    onEvent: (OriginalRouteEvent) -> Unit
) {
    val context = LocalContext.current
    val result = remember {
        mutableStateOf("")
    }
    val code = remember {
        mutableIntStateOf(0)
    }
    val originalBaseUrl = remember {
        mutableStateOf("https://")
    }
    val originalPath = remember {
        mutableStateOf("")
    }
    val originalMethodExpanded = remember {
        mutableStateOf(false)
    }
    val originalMethod = remember {
        mutableStateOf(MiddlewareHttpMethods.Get)
    }
    val originalBody = remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = Screens.ORIGINAL_ROUTE) {
        eventFlow.collectLatest {
            when (it) {
                is OriginalRouteViewModel.UiEvent.ShowError -> {
                    context.showToast(it.message)
                }
                is OriginalRouteViewModel.UiEvent.ShowOriginalRouteSuccess -> {
                    context.showToast("Called original route successfully!")
                }

                is OriginalRouteViewModel.UiEvent.ShowResult -> {
                    code.intValue = it.code
                    result.value = it.result
                }

                OriginalRouteViewModel.UiEvent.NavigateToNextStep -> {
                    val oldFields = generateOldBodyFieldsFromJson(result.value)
                    val args = CreateRouteArgs(
                        originalResponse = result.value,
                        originalBaseUrl = originalBaseUrl.value,
                        originalPath = originalPath.value,
                        originalMethod = originalMethod.value,
                        originalBody = originalBody.value,
                        originalQueries = state.originalQueries,
                        originalHeaders = state.originalHeaders,
                        oldBodyFields = oldFields.mapValues { old ->
                            AndroidOldBodyField.fromOldBodyField(old.value)
                        }
                    )
                    navController.currentBackStackEntry?.arguments?.putParcelable("CreateRouteArgs", args)
                    navController.navigate(Screens.FILL_ROUTE_FIELDS.name)
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            GenericTopAppBar(navController, "Step 1: Original Route")
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                DefaultTextButton(
                    modifier = Modifier.fillMaxWidth()
                        .padding(
                            vertical = 8.dp,
                            horizontal = 32.dp
                        ),
                    text = "Move Forward",
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    enabled = state.isLoading.not(),
                    onClick = {
                        onEvent(
                            OriginalRouteEvent.MoveForward(
                                result = result.value,
                                code = code.intValue,
                                originalBaseUrl = originalBaseUrl.value,
                                originalPath = originalPath.value,
                                originalMethod = originalMethod.value,
                                originalBody = originalBody.value
                            )
                        )
                    }
                )
            }

            item {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    MethodBox(
                        modifier = Modifier
                            .fillMaxWidth(0.2f)
                            .clickable {
                                originalMethodExpanded.value = true
                            },
                        method = originalMethod.value,
                        text = "Original Method"
                    )
                    DropdownMenu(
                        expanded = originalMethodExpanded.value,
                        onDismissRequest = { originalMethodExpanded.value = false }
                    ) {
                        MiddlewareHttpMethods.entries.forEach {
                            DropdownMenuItem(
                                text = { MethodBox(it) },
                                onClick = {
                                    originalMethod.value = it
                                    originalMethodExpanded.value = false
                                }
                            )
                        }
                    }
                }
            }

            item {
                DefaultTextField(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    text = originalBaseUrl.value,
                    isEnabled = state.isLoading.not(),
                    label = "Original Base URL",
                    onValueChange = {
                        originalBaseUrl.value = it
                    }
                )
            }

            item {
                DefaultTextField(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    text = originalPath.value,
                    isEnabled = state.isLoading.not(),
                    label = "Original Path",
                    onValueChange = {
                        originalPath.value = it
                    }
                )
            }

            item {
                DefaultTextField(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    text = originalBody.value,
                    isEnabled = state.isLoading.not(),
                    label = "Original Body",
                    isSingleLined = false,
                    onValueChange = {
                        originalBody.value = it
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.padding(6.dp))
            }

            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Original Headers",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            items(state.originalHeaders.keys.toList()) {
                MapElement(
                    key = it,
                    value = state.originalHeaders[it] ?: return@items,
                    isLoading = state.isLoading,
                    isAdded = state.originalHeaders.containsKey(it),
                    onClickAdd = { key, value ->
                        onEvent(OriginalRouteEvent.UpsertOriginalHeader(key, value))
                    },
                    onClickRemove = { key ->
                        onEvent(OriginalRouteEvent.RemoveOriginalHeader(key))
                    },
                )
            }

            item {
                MapElement(
                    isLoading = state.isLoading,
                    isAdded = false,
                    onClickAdd = { key, value ->
                        onEvent(OriginalRouteEvent.UpsertOriginalHeader(key, value))
                    },
                    onClickRemove = { key ->
                        onEvent(OriginalRouteEvent.RemoveOriginalHeader(key))
                    },
                )
            }

            item {
                Spacer(modifier = Modifier.padding(6.dp))
            }

            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Original Queries",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            items(state.originalQueries.keys.toList()) {
                MapElement(
                    key = it,
                    value = state.originalQueries[it] ?: return@items,
                    isLoading = state.isLoading,
                    isAdded = state.originalQueries.containsKey(it),
                    onClickAdd = { key, value ->
                        onEvent(OriginalRouteEvent.UpsertOriginalQuery(key, value))
                    },
                    onClickRemove = { key ->
                        onEvent(OriginalRouteEvent.RemoveOriginalQuery(key))
                    },
                )
            }

            item {
                MapElement(
                    isLoading = state.isLoading,
                    isAdded = false,
                    onClickAdd = { key, value ->
                        onEvent(OriginalRouteEvent.UpsertOriginalQuery(key, value))
                    },
                    onClickRemove = { key ->
                        onEvent(OriginalRouteEvent.RemoveOriginalQuery(key))
                    },
                )
            }

            item {
                TestColumn(
                    isLoading = state.isLoading,
                    result = if (result.value.isNotBlank()) {
                        "Code: ${code.intValue}\nBody:\n${result.value}"
                    } else {
                        ""
                    },
                ) {
                    result.value = ""
                    onEvent(
                        OriginalRouteEvent.TestOriginalRoute(
                            originalBaseUrl = originalBaseUrl.value,
                            originalPath = originalPath.value,
                            originalMethod = originalMethod.value,
                            originalBody = originalBody.value
                        )
                    )
                }
            }
        }
    }
}
