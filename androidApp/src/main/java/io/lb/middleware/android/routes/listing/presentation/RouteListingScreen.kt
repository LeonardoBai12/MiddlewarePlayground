@file:OptIn(ExperimentalMaterial3Api::class)

package io.lb.middleware.android.routes.listing.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import io.lb.middleware.android.core.presentation.Screens
import io.lb.middleware.android.core.presentation.components.HomeAppBar
import io.lb.middleware.android.core.presentation.components.DefaultCard
import io.lb.middleware.android.core.presentation.showToast
import io.lb.middleware.android.routes.details.model.AndroidMappedRoute
import io.lb.middleware.common.shared.middleware.model.MappedRoute
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.shared.presentation.middleware.listing.RoutesEvent
import io.lb.middleware.shared.presentation.middleware.listing.RoutesState
import io.lb.middleware.shared.presentation.middleware.listing.RoutesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val GROUP_BY_APIS = 0

@Composable
fun RouteListingScreen(
    state: RoutesState,
    navController: NavHostController,
    eventFlow: CommonFlow<RoutesViewModel.UiEvent>,
    drawerState: DrawerState,
    onEvent: (RoutesEvent) -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val radioGroup = listOf(
        "Group by APIs",
        "Show all routes"
    )
    val selectedRadio = remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(Unit) {
        if (state.routes.isEmpty()) {
            delay(500)
        }
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
            HomeAppBar(
                onNavigationIconClick = {
                    scope.launch {
                        if (drawerState.isOpen) {
                            drawerState.close()
                        } else {
                            drawerState.open()
                        }
                    }
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onEvent(RoutesEvent.GetRoutes)
                        }
                    ) {
                        if (state.isLoading.not()) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = "Add Route"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = if (state.isLoading) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                } else {
                    MaterialTheme.colorScheme.primary
                },
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape,
                onClick = {
                    if (state.isLoading.not()) {
                        navController.navigate(Screens.ORIGINAL_ROUTE.name)
                    }
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
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                radioGroup.forEach {
                    RadioButton(
                        enabled = state.isLoading.not(),
                        selected = selectedRadio.intValue == radioGroup.indexOf(it),
                        onClick = {
                            selectedRadio.intValue = radioGroup.indexOf(it)
                        }
                    )
                    Text(
                        modifier = Modifier.clickable(enabled = state.isLoading.not()) {
                            selectedRadio.intValue = radioGroup.indexOf(it)
                        },
                        text = it,
                        textAlign = TextAlign.Start,
                    )
                }
            }
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(top = 16.dp)
                )
                return@Scaffold
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (selectedRadio.intValue == GROUP_BY_APIS) {
                    items(state.apis.keys.toList()) { api ->
                        val routes = state.apis[api] ?: return@items
                        ApiCard(navController, api, routes)
                    }
                } else {
                    items(state.routes) {
                        RouteCard(navController, it)
                    }
                }
            }
        }
    }
}

@Composable
private fun ApiCard(
    navController: NavHostController,
    api: String,
    routes: List<MappedRoute>
) {
    val isCollapsed = remember { mutableStateOf(false) }

    DefaultCard(
        modifier = Modifier.padding(
            start = 16.dp,
            end = 16.dp,
            bottom = 8.dp
        ),
        onClick = {
            isCollapsed.value = !isCollapsed.value
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.9f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Original Base URL",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = api.substringAfterLast("://")
                        .substringBeforeLast("/"),
                    fontSize = 12.sp,
                )
            }
            Icon(
                imageVector = if (isCollapsed.value)
                    Icons.Default.ArrowDropDown
                else
                    Icons.Default.ArrowRight,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "Arrow",
            )
        }
    }
    AnimatedVisibility(isCollapsed.value) {
        Column {
            routes.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.padding(12.dp))
                    Icon(
                        modifier = Modifier.size(12.dp),
                        imageVector = Icons.Default.Circle,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = "Circle",
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    RouteCard(
                        navController = navController,
                        route = it,
                        hideApiData = true
                    )
                }
            }
        }
    }
}

@Composable
private fun RouteCard(
    navController: NavHostController,
    route: MappedRoute,
    hideApiData: Boolean = false
) {
    DefaultCard(
        modifier = Modifier.padding(
            start = if (hideApiData) 0.dp else 16.dp,
            end = 16.dp,
            bottom = 8.dp
        ),
        onClick = {
            val androidRoute = AndroidMappedRoute.fromMappedRoute(route)
            navController.currentBackStackEntry?.arguments?.putParcelable("CreateRouteArgs", androidRoute)
            navController.navigate(Screens.ROUTE_DETAILS.name)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            if (hideApiData.not()) {
                Text(
                    text = "Original Base URL",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = route.originalBaseUrl.substringAfterLast("://")
                        .substringBeforeLast("/"),
                    fontSize = 12.sp,
                )
                Spacer(modifier = Modifier.padding(4.dp))
            }
            Text(
                text = "Original Path",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = route.originalPath,
                fontSize = 12.sp,
            )
            Spacer(modifier = Modifier.padding(4.dp))

            Text(
                text = "Mapped Path",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = route.path,
                fontSize = 12.sp,
            )
        }
    }
}
