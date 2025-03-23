package io.lb.middleware.android.splash.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import io.lb.middleware.android.R
import io.lb.middleware.android.core.presentation.Screens
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.shared.presentation.splash.SplashEvent
import io.lb.middleware.shared.presentation.splash.SplashViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SplashScreen(
    navController: NavController,
    eventFlow: CommonFlow<SplashViewModel.UiEvent>,
    onEvent: (SplashEvent) -> Unit
) {
    LaunchedEffect(key1 = Screens.SPLASH) {
        eventFlow.collectLatest {
            when (it) {
                is SplashViewModel.UiEvent.NavigateToSignUp -> {
                    navController.navigate(Screens.SIGN_UP.name) {
                        popUpTo(Screens.SPLASH.name) {
                            inclusive = true
                        }
                    }
                }
                is SplashViewModel.UiEvent.NavigateToHome -> {
                    navController.navigate(Screens.ROUTE_LISTING.name) {
                        popUpTo(Screens.SPLASH.name) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(1500)
        onEvent(SplashEvent.GetCurrentUser)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier.fillMaxSize(0.7f),
                painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                contentDescription = "Logo"
            )
        }
    }
}
