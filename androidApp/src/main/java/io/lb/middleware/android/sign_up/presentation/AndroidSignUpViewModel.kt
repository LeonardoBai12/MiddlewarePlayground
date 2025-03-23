package io.lb.middleware.android.sign_up.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lb.middleware.shared.presentation.sign_up.SignUpEvent
import io.lb.middleware.shared.presentation.sign_up.SignUpViewModel
import io.middleware.sign_up.domain.use_cases.LoginUseCase
import io.middleware.sign_up.domain.use_cases.SignUpUseCase
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@HiltViewModel
class AndroidSignUpViewModel @Inject constructor(
    signUpUseCase: SignUpUseCase,
    loginUseCase: LoginUseCase
) : ViewModel() {
    private val viewModel by lazy {
        SignUpViewModel(
            viewModelScope,
            signUpUseCase,
            loginUseCase,
        )
    }
    val state = viewModel.state
    val eventFlow = viewModel.eventFlow

    fun onEvent(event: SignUpEvent) {
        viewModel.onEvent(event)
    }
}
