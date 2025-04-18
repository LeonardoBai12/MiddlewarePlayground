@file:OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)

package io.lb.middleware.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
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
import io.lb.middleware.android.createroute.presentation.review.ReviewScreen
import io.lb.middleware.android.createroute.presentation.fillpreconfigs.AndroidFillPreConfigsViewModel
import io.lb.middleware.android.createroute.presentation.fillpreconfigs.FillPreConfigsScreen
import io.lb.middleware.android.createroute.presentation.fillroutes.AndroidFillRouteFieldsViewModel
import io.lb.middleware.android.createroute.presentation.fillroutes.FillRouteFieldsScreen
import io.lb.middleware.android.createroute.presentation.model.CreateRouteArgs
import io.lb.middleware.android.createroute.presentation.originalroute.AndroidOriginalRouteViewModel
import io.lb.middleware.android.createroute.presentation.originalroute.OriginalRouteScreen
import io.lb.middleware.android.createroute.presentation.preview.AndroidPreviewViewModel
import io.lb.middleware.android.createroute.presentation.preview.PreviewScreen
import io.lb.middleware.android.createroute.presentation.review.AndroidReviewViewModel
import io.lb.middleware.android.history.presentation.AndroidHistoryViewModel
import io.lb.middleware.android.routes.details.model.AndroidMappedRoute
import io.lb.middleware.android.routes.details.presentation.AndroidRouteDetailsViewModel
import io.lb.middleware.android.routes.details.presentation.RouteDetailsScreen
import io.lb.middleware.android.routes.listing.presentation.AndroidRoutesViewModel
import io.lb.middleware.android.routes.listing.presentation.RouteListingScreen
import io.lb.middleware.android.user.presentation.UserDrawer
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
            val routeViewModel = hiltViewModel<AndroidRoutesViewModel>()
            val userViewModel = hiltViewModel<AndroidUserViewModel>()
            val routeState by routeViewModel.state.collectAsState()
            val userState by userViewModel.state.collectAsState()
            val userEventFlow = userViewModel.eventFlow
            val routeEventFlow = routeViewModel.eventFlow
            val drawerState = rememberDrawerState(DrawerValue.Closed)

            UserDrawer(
                navController = navController,
                state = userState,
                eventFlow = userEventFlow,
                drawerState = drawerState,
                onEvent = { userViewModel.onEvent(it) }
            ) {
                RouteListingScreen(
                    navController = navController,
                    state = routeState,
                    eventFlow = routeEventFlow,
                    drawerState = drawerState,
                    onEvent = { routeViewModel.onEvent(it) }
                )
            }
        }
        composable(
            Screens.ROUTE_DETAILS.name
        ) {
            val viewModel = hiltViewModel<AndroidRouteDetailsViewModel>()
            val state by viewModel.state.collectAsState()
            val eventFlow = viewModel.eventFlow
            val route = navController.previousBackStackEntry?.arguments
                ?.getParcelable<AndroidMappedRoute>("CreateRouteArgs")
            RouteDetailsScreen(
                route = route?.toMappedRoute(),
                navController = navController,
                state = state,
                eventFlow = eventFlow,
                onEvent = { viewModel.onEvent(it) }
            )
        }
        composable(Screens.FILL_ROUTE_FIELDS.name) {
            val viewModel = hiltViewModel<AndroidFillRouteFieldsViewModel>()
            val state by viewModel.state.collectAsState()
            val eventFlow = viewModel.eventFlow
            val args = navController.previousBackStackEntry
                ?.arguments
                ?.getParcelable<CreateRouteArgs>("CreateRouteArgs")

            FillRouteFieldsScreen(
                navController = navController,
                args = args,
                state = state,
                eventFlow = eventFlow,
                onEvent = { viewModel.onEvent(it) }
            )
        }
        composable(Screens.FILL_PRE_CONFIGS.name) {
            val viewModel = hiltViewModel<AndroidFillPreConfigsViewModel>()
            val state by viewModel.state.collectAsState()
            val eventFlow = viewModel.eventFlow
            val args = navController.previousBackStackEntry
                ?.arguments
                ?.getParcelable<CreateRouteArgs>("CreateRouteArgs")

            FillPreConfigsScreen(
                navController = navController,
                args = args,
                state = state,
                eventFlow = eventFlow,
                onEvent = { viewModel.onEvent(it) }
            )
        }
        composable(Screens.PREVIEW.name) {
            val viewModel = hiltViewModel<AndroidPreviewViewModel>()
            val state by viewModel.state.collectAsState()
            val eventFlow = viewModel.eventFlow
            val args = navController.previousBackStackEntry
                ?.arguments
                ?.getParcelable<CreateRouteArgs>("CreateRouteArgs")

            PreviewScreen(
                navController = navController,
                args = args,
                state = state,
                eventFlow = eventFlow,
                onEvent = { viewModel.onEvent(it) }
            )
        }
        composable(Screens.ORIGINAL_ROUTE.name) {
            val viewModel = hiltViewModel<AndroidOriginalRouteViewModel>()
            val state by viewModel.state.collectAsState()
            val eventFlow = viewModel.eventFlow

            OriginalRouteScreen(
                navController = navController,
                state = state,
                eventFlow = eventFlow,
                onEvent = { viewModel.onEvent(it) }
            )
        }
        composable(Screens.REVIEW.name) {
            val viewModel = hiltViewModel<AndroidReviewViewModel>()
            val state by viewModel.state.collectAsState()
            val eventFlow = viewModel.eventFlow
            val args = navController.previousBackStackEntry
                ?.arguments
                ?.getParcelable<CreateRouteArgs>("CreateRouteArgs")

            ReviewScreen(
                navController = navController,
                args = args,
                state = state,
                eventFlow = eventFlow,
                onEvent = { viewModel.onEvent(it) }
            )
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
