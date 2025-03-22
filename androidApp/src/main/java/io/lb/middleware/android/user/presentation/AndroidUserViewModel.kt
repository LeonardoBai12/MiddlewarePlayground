package io.lb.middleware.android.user.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.lb.middleware.shared.presentation.user.UserEvent
import io.lb.middleware.shared.presentation.user.UserViewModel
import io.middleware.user.domain.use_cases.DeleteUserUseCase
import io.middleware.user.domain.use_cases.GetCurrentUserUseCase
import io.middleware.user.domain.use_cases.LogoutUseCase
import io.middleware.user.domain.use_cases.UpdatePasswordUseCase
import io.middleware.user.domain.use_cases.UpdateUserUseCase

class AndroidUserViewModel(
    getCurrentUserUseCase: GetCurrentUserUseCase,
    updateUserUseCase: UpdateUserUseCase,
    updatePasswordUseCase: UpdatePasswordUseCase,
    deleteUserUseCase: DeleteUserUseCase,
    logoutUseCase: LogoutUseCase
) : ViewModel() {
    private val viewModel by lazy {
        UserViewModel(
            viewModelScope,
            getCurrentUserUseCase,
            updateUserUseCase,
            updatePasswordUseCase,
            deleteUserUseCase,
            logoutUseCase,
        )
    }
    val state = viewModel.state
    val eventFlow = viewModel.eventFlow

    fun onEvent(event: UserEvent) {
        viewModel.onEvent(event)
    }
}
