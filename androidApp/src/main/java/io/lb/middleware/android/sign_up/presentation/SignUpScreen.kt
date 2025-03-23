package io.lb.middleware.android.sign_up.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.lb.middleware.android.core.presentation.components.DefaultTextButton
import io.lb.middleware.android.sign_up.presentation.login.LoginBottomSheetContent
import io.lb.middleware.android.sign_up.presentation.sing_up.SignUpBottomSheetContent
import io.lb.middleware.android.sign_up.presentation.components.HomeLoginBackground
import io.lb.middleware.android.sign_up.presentation.components.HomeLoginHeader
import io.lb.middleware.android.R
import io.lb.middleware.android.core.presentation.Screens
import io.lb.middleware.android.core.presentation.showToast
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.shared.presentation.sign_up.SignUpEvent
import io.lb.middleware.shared.presentation.sign_up.SignUpState
import io.lb.middleware.shared.presentation.sign_up.SignUpViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun SignUpScreen(
    state: SignUpState,
    eventFlow: CommonFlow<SignUpViewModel.UiEvent>,
    onEvent: (SignUpEvent) -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val showBottomSheet = remember { mutableStateOf(false) }
    val isLogin = remember {
        mutableStateOf(true)
    }

    LaunchedEffect(
        key1 = Screens.SIGN_UP.name
    ) {
        eventFlow.collectLatest {
            when (it) {
                is SignUpViewModel.UiEvent.ShowError -> {
                    context.showToast(it.message)
                }
                is SignUpViewModel.UiEvent.ShowLoginSuccess -> {
                    context.showToast("Logged in successfully!")
                }
                is SignUpViewModel.UiEvent.ShowSignUpSuccess -> {
                    context.showToast("Signed up successfully!")
                }
            }
        }
    }

    BackHandler(enabled = bottomSheetState.isVisible) {
        coroutineScope.launch {
            bottomSheetState.hide()
        }
    }

    Scaffold { padding ->
        HomeLoginBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            HomeLoginHeader()
            LoginButtonsColumn(bottomSheetState, showBottomSheet, isLogin)
        }
        if (showBottomSheet.value) {
            ModalBottomSheet(
                sheetState = bottomSheetState,
                containerColor = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
                onDismissRequest = {
                    showBottomSheet.value = false
                }
            ) {
                if (isLogin.value) {
                    LoginBottomSheetContent(
                        isLoading = state.isLoading,
                        onLoginWithEmailAndPassword = { email, password ->
                            onEvent(
                                SignUpEvent.RequestLogin(
                                    email = email,
                                    password = password
                                )
                            )
                        }
                    )
                } else {
                    SignUpBottomSheetContent(
                        isLoading = state.isLoading,
                        onSignInWithEmailAndPassword = { name, email, phone, password, repeatedPassword ->
                            onEvent(
                                SignUpEvent.RequestSignUp(
                                    userName = name,
                                    email = email,
                                    phone = phone,
                                    password = password,
                                    profilePictureUrl = null,
                                    repeatedPassword = repeatedPassword
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginButtonsColumn(
    bottomSheetState: SheetState,
    showBottomSheet: MutableState<Boolean>,
    isLogin: MutableState<Boolean>,
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(36.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DefaultTextButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            text = stringResource(id = R.string.login),
            onClick = {
                coroutineScope.launch {
                    isLogin.value = true
                    if (bottomSheetState.isVisible.not()) {
                        showBottomSheet.value = true
                    }
                }
            },
        )

        DefaultTextButton(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.sign_up),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            onClick = {
                coroutineScope.launch {
                    isLogin.value = false
                    if (bottomSheetState.isVisible.not()) {
                        showBottomSheet.value = true
                    }
                }
            },
        )
    }
}
