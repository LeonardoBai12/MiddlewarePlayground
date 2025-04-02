package io.lb.middleware.android.createroute.presentation.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.lb.middleware.android.core.presentation.Screens
import io.lb.middleware.android.core.presentation.components.DefaultTextButton
import io.lb.middleware.android.core.presentation.components.GenericTopAppBar
import io.lb.middleware.android.core.presentation.components.TestColumn
import io.lb.middleware.android.core.presentation.showToast
import io.lb.middleware.android.createroute.presentation.model.CreateRouteArgs
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.shared.presentation.createroute.originalroute.OriginalRouteEvent
import io.lb.middleware.shared.presentation.createroute.preview.PreviewEvent
import io.lb.middleware.shared.presentation.createroute.preview.PreviewState
import io.lb.middleware.shared.presentation.createroute.preview.PreviewViewModel
import io.lb.middleware.shared.presentation.middleware.details.RouteDetailsEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewScreen(
    navController: NavHostController,
    args: CreateRouteArgs?,
    state: PreviewState,
    eventFlow: CommonFlow<PreviewViewModel.UiEvent>,
    onEvent: (PreviewEvent) -> Unit
) {
    val context = LocalContext.current
    val result = remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = Screens.PREVIEW) {
        eventFlow.collect { event ->
            when (event) {
                is PreviewViewModel.UiEvent.ShowError -> {
                    context.showToast(event.message)
                }
                is PreviewViewModel.UiEvent.ShowResult -> {
                    result.value = event.result
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            GenericTopAppBar(navController, "Step 4: Preview")
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
                    enabled = state.isLoading.not(),
                    onClick = {
                        navController.currentBackStackEntry?.arguments?.putParcelable("CreateRouteArgs", args)
                        navController.navigate(Screens.REVIEW.name)
                    }
                )
            }
            item {
                TestColumn(
                    isLoading = state.isLoading,
                    result = result.value,
                ) {
                    result.value = ""
                    onEvent(
                        PreviewEvent.RequestPreview(
                            response = args?.originalResponse ?: "",
                            newBodyFields = args?.newBodyFields?.mapValues {
                                it.value.toNewBodyField()
                            } ?: emptyMap(),
                            oldBodyFields = args?.oldBodyFields?.mapValues {
                                it.value.toOldBodyField()
                            } ?: emptyMap(),
                            ignoreEmptyValues = args?.ignoreEmptyFields ?: true
                        )
                    )
                }
            }
        }
    }
}
