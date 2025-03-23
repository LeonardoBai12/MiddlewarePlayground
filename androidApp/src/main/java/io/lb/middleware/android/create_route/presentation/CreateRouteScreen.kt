package io.lb.middleware.android.create_route.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import io.lb.middleware.android.core.presentation.Screens
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.shared.presentation.create_route.CreateRouteEvent
import io.lb.middleware.shared.presentation.create_route.CreateRouteState
import io.lb.middleware.shared.presentation.create_route.CreateRouteViewModel

@Composable
fun RouteDetailsScreen(
    navController: NavHostController,
    state: CreateRouteState,
    eventFlow: CommonFlow<CreateRouteViewModel.UiEvent>,
    onEvent: (CreateRouteEvent) -> Unit
) {
    LaunchedEffect(key1 = Screens.CREATE_ROUTE) {

    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape,
                onClick = {

                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Route"
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Create Route Screen")
        }
    }
}
