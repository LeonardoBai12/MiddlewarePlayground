package io.lb.middleware.android.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lb.middleware.shared.presentation.splash.SplashEvent
import io.lb.middleware.shared.presentation.splash.SplashViewModel
import io.middleware.splash.domain.use_cases.GetCurrentUserOnInitUseCase
import javax.inject.Inject

@HiltViewModel
class AndroidSplashViewModel @Inject constructor(
    getCurrentUserOnInitUseCase: GetCurrentUserOnInitUseCase
) : ViewModel() {
    private val viewModel by lazy {
        SplashViewModel(
            viewModelScope,
            getCurrentUserOnInitUseCase,
        )
    }
    val eventFlow = viewModel.eventFlow

    fun onEvent(event: SplashEvent) {
        viewModel.onEvent(event)
    }
}
