package io.lb.middleware.shared.presentation.splash

import io.lb.middleware.common.state.toCommonStateFlow
import io.middleware.splash.domain.use_cases.GetCurrentUserOnInitUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel(
    private val getCurrentUserOnInitUseCase: GetCurrentUserOnInitUseCase,
    coroutineScope: CoroutineScope?
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(SplashState())
    val state = _state.toCommonStateFlow()

    fun onEvent(event: SplashEvent) {
        when (event) {
            is SplashEvent.GetCurrentUser -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(userData = getCurrentUserOnInitUseCase.invoke())
                    }
                }
            }
        }
    }
}
