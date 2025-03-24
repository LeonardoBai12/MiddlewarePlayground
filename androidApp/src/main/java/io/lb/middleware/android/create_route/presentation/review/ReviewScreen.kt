package io.lb.middleware.android.create_route.presentation.review

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
import io.lb.middleware.shared.presentation.create_route.review.ReviewEvent
import io.lb.middleware.shared.presentation.create_route.review.ReviewState
import io.lb.middleware.shared.presentation.create_route.review.ReviewViewModel

@Composable
fun ReviewScreen(
    navController: NavHostController,
    state: ReviewState,
    eventFlow: CommonFlow<ReviewViewModel.UiEvent>,
    onEvent: (ReviewEvent) -> Unit
) {
    LaunchedEffect(key1 = Screens.REVIEW) {

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
