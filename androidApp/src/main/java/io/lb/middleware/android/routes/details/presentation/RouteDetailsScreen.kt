@file:OptIn(ExperimentalMaterial3Api::class)

package io.lb.middleware.android.routes.details.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import io.lb.middleware.android.core.presentation.Screens
import io.lb.middleware.android.core.presentation.components.GenericTopAppBar
import io.lb.middleware.android.core.presentation.components.MethodBox
import io.lb.middleware.android.core.presentation.components.RouteDetails
import io.lb.middleware.android.core.presentation.components.TestColumn
import io.lb.middleware.android.core.presentation.showToast
import io.lb.middleware.common.shared.middleware.model.MappedRoute
import io.lb.middleware.common.shared.util.beautifyJson
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.shared.presentation.middleware.details.RouteDetailsEvent
import io.lb.middleware.shared.presentation.middleware.details.RouteDetailsState
import io.lb.middleware.shared.presentation.middleware.details.RouteDetailsViewModel
import kotlinx.coroutines.flow.collectLatest

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
                        MethodBox(
                            route.originalMethod,
                            "Original Methods",
                            Modifier.width(80.dp)
                        )
                        Spacer(modifier = Modifier.padding(16.dp))
                        MethodBox(
                            route.method,
                            "Mapped Methods",
                            Modifier.width(80.dp)
                        )
                    }
                    Spacer(modifier = Modifier.padding(12.dp))
                }
            }

            item {
                RouteDetails(
                    originalQueries = route.originalQueries,
                    originalHeaders = route.originalHeaders,
                    originalBody = route.originalBody,
                    preConfiguredQueries = route.preConfiguredQueries,
                    preConfiguredHeaders = route.preConfiguredHeaders,
                    preConfiguredBody = route.preConfiguredBody,
                )
            }

            item {
                TestColumn(
                    isLoading = state.isLoading,
                    result = result.value,
                ) {
                    result.value = ""
                    onEvent(RouteDetailsEvent.TestRoute)
                }
            }
        }
    }
}
