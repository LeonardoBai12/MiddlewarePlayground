package io.lb.middleware.android.createroute.presentation.review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import io.lb.middleware.android.createroute.presentation.model.CreateRouteArgs
import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.shared.presentation.createroute.review.ReviewEvent
import io.lb.middleware.shared.presentation.createroute.review.ReviewState
import io.lb.middleware.shared.presentation.createroute.review.ReviewViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    navController: NavHostController,
    args: CreateRouteArgs?,
    state: ReviewState,
    eventFlow: CommonFlow<ReviewViewModel.UiEvent>,
    onEvent: (ReviewEvent) -> Unit
) {
    val context = LocalContext.current
    val result = remember {
        mutableStateOf("")
    }
    val isFinished = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Screens.REVIEW) {
        eventFlow.collectLatest {
            when (it) {
                is ReviewViewModel.UiEvent.ShowCreatedRouteMessage -> {
                    context.showToast("Route Created!")
                    isFinished.value = true
                    result.value = it.result
                }

                is ReviewViewModel.UiEvent.ShowError -> {
                    context.showToast(it.message)
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            GenericTopAppBar(navController, "Step 5/5: Review & Create Route")
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
                        text = args?.originalBaseUrl?.substringAfterLast("://") ?: "",
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
                        text = args?.originalPath?.substringAfterLast("://") ?: "",
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
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Mapped Path",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = args?.mappedPath?.substringAfterLast("://") ?: "",
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
                            args?.originalMethod,
                            "Original Methods",
                            Modifier.width(80.dp)
                        )
                        Spacer(modifier = Modifier.padding(16.dp))
                        MethodBox(
                            args?.originalMethod,
                            "Mapped Methods",
                            Modifier.width(80.dp)
                        )
                    }
                    Spacer(modifier = Modifier.padding(12.dp))
                }
            }

            item {
                RouteDetails(
                    originalQueries = args?.originalQueries,
                    originalHeaders = args?.originalHeaders,
                    originalBody = args?.originalBody,
                    preConfiguredQueries = args?.preConfiguredQueries,
                    preConfiguredHeaders = args?.preConfiguredHeaders,
                    preConfiguredBody = args?.preConfiguredBody,
                )
            }

            item {
                TestColumn(
                    isLoading = state.isLoading && isFinished.value.not(),
                    result = result.value,
                    idleText = "Create Route",
                    progressText = "Creating Route",
                ) {
                    result.value = ""
                    onEvent(
                        ReviewEvent.CreateMappedRoute(
                            originalBaseUrl = args?.originalBaseUrl ?: "",
                            originalPath = args?.originalPath ?: "",
                            originalMethod = args?.originalMethod ?: MiddlewareHttpMethods.Get,
                            originalBody = args?.originalBody ?: "",
                            originalQueries = args?.originalQueries ?: emptyMap(),
                            originalHeaders = args?.originalHeaders ?: emptyMap(),
                            newBodyFields = args?.newBodyFields?.mapValues {
                                it.value.toNewBodyField()
                            } ?: emptyMap(),
                            oldBodyFields = args?.oldBodyFields?.mapValues {
                                it.value.toOldBodyField()
                            } ?: emptyMap(),
                            preConfiguredQueries = args?.preConfiguredQueries ?: emptyMap(),
                            preConfiguredHeaders = args?.preConfiguredHeaders ?: emptyMap(),
                            mappedPath = args?.mappedPath ?: "",
                            mappedMethod = args?.mappedMethod ?: MiddlewareHttpMethods.Get,
                            preConfiguredBody = args?.preConfiguredBody ?: "",
                            ignoreEmptyValues = args?.ignoreEmptyFields ?: true
                        )
                    )
                }
            }
        }
    }
}
