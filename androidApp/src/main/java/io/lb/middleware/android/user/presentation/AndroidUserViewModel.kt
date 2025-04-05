package io.lb.middleware.android.user.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lb.middleware.shared.presentation.user.UserEvent
import io.lb.middleware.shared.presentation.user.UserViewModel
import io.middleware.user.domain.usecases.DeleteUserUseCase
import io.middleware.user.domain.usecases.GetCurrentUserUseCase
import io.middleware.user.domain.usecases.LogoutUseCase
import io.middleware.user.domain.usecases.UpdatePasswordUseCase
import io.middleware.user.domain.usecases.UpdateUserUseCase
import javax.inject.Inject

@HiltViewModel
class AndroidUserViewModel @Inject constructor(
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
