package io.lb.middleware.shared.presentation.splash

import io.lb.middleware.common.shared.user.UserData
import io.lb.middleware.common.state.toCommonFlow
import io.middleware.splash.domain.use_cases.GetCurrentUserOnInitUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    coroutineScope: CoroutineScope?,
    private val getCurrentUserOnInitUseCase: GetCurrentUserOnInitUseCase,
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow().toCommonFlow()

    sealed class UiEvent {
        data object NavigateToSignUp : UiEvent()
        data class NavigateToHome(val userData: UserData) : UiEvent()
    }

    fun onEvent(event: SplashEvent) {
        when (event) {
            is SplashEvent.GetCurrentUser -> {
                viewModelScope.launch {
                    getCurrentUserOnInitUseCase.invoke()?.let {
                        _eventFlow.emit(UiEvent.NavigateToHome(it))
                    } ?: run {
                        _eventFlow.emit(UiEvent.NavigateToSignUp)
                    }
                }
            }
        }
    }
}
