package io.lb.middleware.android.createroute.presentation.originalroute

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lb.middleware.shared.presentation.createroute.originalroute.OriginalRouteEvent
import io.lb.middleware.shared.presentation.createroute.originalroute.OriginalRouteViewModel
import io.middleware.api.domain.usecases.TestOriginalRouteUseCase
import javax.inject.Inject

@HiltViewModel
class AndroidOriginalRouteViewModel @Inject constructor(
    testOriginalRouteUseCase: TestOriginalRouteUseCase,
) : ViewModel() {
    private val viewModel by lazy {
        OriginalRouteViewModel(
            viewModelScope,
            testOriginalRouteUseCase,
        )
    }
    val state = viewModel.state
    val eventFlow = viewModel.eventFlow

    fun onEvent(event: OriginalRouteEvent) {
        viewModel.onEvent(event)
    }
}
