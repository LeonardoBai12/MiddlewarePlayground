package io.lb.middleware.android.routes.listing.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.lb.middleware.android.core.presentation.Screens
import io.lb.middleware.android.core.presentation.components.DefaultTextButton
import io.lb.middleware.android.core.presentation.showToast
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.shared.presentation.user.UserEvent
import io.lb.middleware.shared.presentation.user.UserState
import io.lb.middleware.shared.presentation.user.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun UserDrawer(
    navController: NavController,
    state: UserState,
    eventFlow: CommonFlow<UserViewModel.UiEvent>,
    onEvent: (UserEvent) -> Unit,
    drawerState: DrawerState,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userData = state.userData

    LaunchedEffect(key1 = Screens.USER.name + "Drawer") {
        eventFlow.collectLatest {
            when (it) {
                UserViewModel.UiEvent.ShowLoggedOutMessage -> {
                    context.showToast("Logged out")
                    navController.navigate(Screens.SPLASH.name) {
                        popUpTo(Screens.SPLASH.name) {
                            inclusive = true
                        }
                    }
                }
                else -> {
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        onEvent(UserEvent.GetCurrentUser)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(280.dp),
                drawerContainerColor = MaterialTheme.colorScheme.background,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Spacer(modifier = Modifier.padding(16.dp))
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userData?.userName?.first()?.uppercase() ?: "?",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 48.sp
                        )
                    }

                    Spacer(modifier = Modifier.padding(8.dp))

                    Text(
                        text = userData?.userName ?: "No name",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.padding(4.dp))

                    Text(
                        text = userData?.email ?: "No email",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.padding(4.dp))

                    Text(
                        text = userData?.phone ?: "No phone",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.padding(16.dp))

                    DefaultTextButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Logout",
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        onClick = {
                            scope.launch { drawerState.close() }
                            onEvent(UserEvent.Logout)
                        }
                    )
                }
            }
        },
        content = content
    )
}
