package io.lb.middleware.android.createroute.presentation.fillpreconfigs

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
import io.lb.middleware.android.core.presentation.showToast
import io.lb.middleware.android.createroute.presentation.model.CreateRouteArgs
import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.shared.presentation.createroute.fillpreconfigs.FillPreConfigsEvent
import io.lb.middleware.shared.presentation.createroute.fillpreconfigs.FillPreConfigsState
import io.lb.middleware.shared.presentation.createroute.fillpreconfigs.FillPreConfigsViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FillPreConfigsScreen(
    navController: NavHostController,
    state: FillPreConfigsState,
    args: CreateRouteArgs?,
    eventFlow: CommonFlow<FillPreConfigsViewModel.UiEvent>,
    onEvent: (FillPreConfigsEvent) -> Unit
) {
    val context = LocalContext.current
    val mappedPath = remember {
        mutableStateOf(args?.originalPath ?: "")
    }
    val mappedMethodExpanded = remember {
        mutableStateOf(false)
    }
    val mappedMethod = remember {
        mutableStateOf(args?.mappedMethod)
    }
    val mappedBody = remember {
        mutableStateOf(args?.preConfiguredBody ?: "")
    }

    LaunchedEffect(key1 = Screens.FILL_PRE_CONFIGS) {
        eventFlow.collectLatest {
            when (it) {
                is FillPreConfigsViewModel.UiEvent.ShowError -> {
                    context.showToast(it.message)
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            GenericTopAppBar(navController, "Step 3: Fill Pre Configs (Optional)")
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(12.dp)
                .fillMaxSize(),
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
                    onClick = {
                        
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
                                mappedMethodExpanded.value = true
                            },
                        method = mappedMethod.value,
                        text = "Original Method"
                    )
                    DropdownMenu(
                        expanded = mappedMethodExpanded.value,
                        onDismissRequest = { mappedMethodExpanded.value = false }
                    ) {
                        MiddlewareHttpMethods.entries.forEach {
                            DropdownMenuItem(
                                text = { MethodBox(it) },
                                onClick = {
                                    mappedMethod.value = it
                                    mappedMethodExpanded.value = false
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
                    text = mappedPath.value,
                    label = "Mapped Path",
                    onValueChange = {
                        mappedPath.value = it
                    }
                )
            }

            item {
                DefaultTextField(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    text = mappedBody.value,
                    label = "Pre-Configured Body",
                    isSingleLined = false,
                    onValueChange = {
                        mappedBody.value = it
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.padding(6.dp))
            }

            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Pre-Configured Headers",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            items(state.preConfiguredHeaders.keys.toList()) {
                MapElement(
                    key = it,
                    value = state.preConfiguredHeaders[it] ?: return@items,
                    isLoading = false,
                    isAdded = state.preConfiguredHeaders.containsKey(it),
                    onClickAdd = { key, value ->
                        onEvent(FillPreConfigsEvent.UpsertPreConfiguredHeader(key, value))
                    },
                    onClickRemove = { key ->
                        onEvent(FillPreConfigsEvent.RemovePreConfiguredHeader(key))
                    },
                )
            }

            item {
                MapElement(
                    isLoading = false,
                    isAdded = false,
                    onClickAdd = { key, value ->
                        onEvent(FillPreConfigsEvent.UpsertPreConfiguredHeader(key, value))
                    },
                    onClickRemove = { key ->
                        onEvent(FillPreConfigsEvent.RemovePreConfiguredHeader(key))
                    },
                )
            }

            item {
                Spacer(modifier = Modifier.padding(6.dp))
            }

            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Pre-Configured Queries",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            items(state.preConfiguredQueries.keys.toList()) {
                MapElement(
                    key = it,
                    value = state.preConfiguredQueries[it] ?: return@items,
                    isLoading = false,
                    isAdded = state.preConfiguredQueries.containsKey(it),
                    onClickAdd = { key, value ->
                        onEvent(FillPreConfigsEvent.UpsertPreConfiguredQuery(key, value))
                    },
                    onClickRemove = { key ->
                        onEvent(FillPreConfigsEvent.RemovePreConfiguredQuery(key))
                    },
                )
            }

            item {
                MapElement(
                    isLoading = false,
                    isAdded = false,
                    onClickAdd = { key, value ->
                        onEvent(FillPreConfigsEvent.UpsertPreConfiguredQuery(key, value))
                    },
                    onClickRemove = { key ->
                        onEvent(FillPreConfigsEvent.RemovePreConfiguredQuery(key))
                    },
                )
            }
        }
    }
}
