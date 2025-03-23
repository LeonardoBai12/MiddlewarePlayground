@file:OptIn(ExperimentalMaterial3Api::class)

package io.lb.middleware.android.routes.listing.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import io.lb.middleware.android.core.presentation.Screens
import io.lb.middleware.android.core.presentation.components.DefaultAppBar
import io.lb.middleware.android.core.presentation.showToast
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.shared.presentation.middleware.listing.RoutesEvent
import io.lb.middleware.shared.presentation.middleware.listing.RoutesState
import io.lb.middleware.shared.presentation.middleware.listing.RoutesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RouteListingScreen(
    state: RoutesState,
    navController: NavHostController,
    eventFlow: CommonFlow<RoutesViewModel.UiEvent>,
    onEvent: (RoutesEvent) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        delay(1500)
        onEvent(RoutesEvent.GetRoutes)
    }

    LaunchedEffect(key1 = Screens.ROUTE_LISTING) {
        eventFlow.collectLatest {
            when (it) {
                is RoutesViewModel.UiEvent.ShowError -> {
                    context.showToast(it.message)
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DefaultAppBar {

            }
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape,
                onClick = {
                    navController.navigate(Screens.CREATE_ROUTE.name)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Route"
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(state.routes) {
                Text(text = it.uuid)
            }
        }
    }
}
