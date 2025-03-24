@file:OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)

package io.lb.middleware.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.lb.middleware.android.core.presentation.PlaygroundTheme
import io.lb.middleware.android.core.presentation.Screens
import io.lb.middleware.android.create_route.presentation.review.AndroidReviewViewModel
import io.lb.middleware.android.history.presentation.AndroidHistoryViewModel
import io.lb.middleware.android.routes.details.model.AndroidMappedRoute
import io.lb.middleware.android.routes.details.presentation.AndroidRouteDetailsViewModel
import io.lb.middleware.android.routes.details.presentation.RouteDetailsScreen
import io.lb.middleware.android.routes.listing.presentation.AndroidRoutesViewModel
import io.lb.middleware.android.routes.listing.presentation.RouteListingScreen
import io.lb.middleware.android.sign_up.presentation.AndroidSignUpViewModel
import io.lb.middleware.android.sign_up.presentation.SignUpScreen
import io.lb.middleware.android.splash.presentation.AndroidSplashViewModel
import io.lb.middleware.android.splash.presentation.SplashScreen
import io.lb.middleware.android.user.presentation.AndroidUserViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaygroundTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PlaygroundRoot()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaygroundRoot() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.SPLASH.name
    ) {
        composable(Screens.SPLASH.name) {
            val viewModel = hiltViewModel<AndroidSplashViewModel>()
            val eventFlow = viewModel.eventFlow

            SplashScreen(
                navController = navController,
                eventFlow = eventFlow,
                onEvent = { viewModel.onEvent(it) }
            )
        }
        composable(Screens.SIGN_UP.name) {
            val viewModel = hiltViewModel<AndroidSignUpViewModel>()
            val state by viewModel.state.collectAsState()
            val eventFlow = viewModel.eventFlow

            SignUpScreen(
                navController = navController,
                state = state,
                eventFlow = eventFlow,
                onEvent = { viewModel.onEvent(it) }
            )
        }
        composable(Screens.ROUTE_LISTING.name) {
            val viewModel = hiltViewModel<AndroidRoutesViewModel>()
            val state by viewModel.state.collectAsState()
            val eventFlow = viewModel.eventFlow

            RouteListingScreen(
                navController = navController,
                state = state,
                eventFlow = eventFlow,
                onEvent = { viewModel.onEvent(it) }
            )
        }
        composable(
            Screens.ROUTE_DETAILS.name
        ) {
            val viewModel = hiltViewModel<AndroidRouteDetailsViewModel>()
            val state by viewModel.state.collectAsState()
            val eventFlow = viewModel.eventFlow
            val route = navController.previousBackStackEntry?.arguments?.getParcelable<AndroidMappedRoute>("route")
            RouteDetailsScreen(
                route = route?.toMappedRoute(),
                navController = navController,
                state = state,
                eventFlow = eventFlow,
                onEvent = { viewModel.onEvent(it) }
            )
        }
        composable(Screens.FILL_ROUTES.name) {
            val viewModel = hiltViewModel<AndroidReviewViewModel>()
            val state by viewModel.state.collectAsState()
            val eventFlow = viewModel.eventFlow

        }
        composable(Screens.PREVIEW.name) {
            val viewModel = hiltViewModel<AndroidReviewViewModel>()
            val state by viewModel.state.collectAsState()
            val eventFlow = viewModel.eventFlow

        }
        composable(Screens.ORIGINAL_ROUTE.name) {
            val viewModel = hiltViewModel<AndroidReviewViewModel>()
            val state by viewModel.state.collectAsState()
            val eventFlow = viewModel.eventFlow

        }
        composable(Screens.REVIEW.name) {
            val viewModel = hiltViewModel<AndroidReviewViewModel>()
            val state by viewModel.state.collectAsState()
            val eventFlow = viewModel.eventFlow

        }
        composable(Screens.HISTORY.name) {
            val viewModel = hiltViewModel<AndroidHistoryViewModel>()
            val state by viewModel.state.collectAsState()
            val eventFlow = viewModel.eventFlow

        }
        composable(Screens.USER.name) {
            val viewModel = hiltViewModel<AndroidUserViewModel>()
            val state by viewModel.state.collectAsState()
            val eventFlow = viewModel.eventFlow

        }
    }
}
