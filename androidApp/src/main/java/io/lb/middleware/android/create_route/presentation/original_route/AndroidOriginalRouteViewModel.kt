package io.lb.middleware.android.create_route.presentation.original_route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lb.middleware.shared.presentation.create_route.original_route.OriginalRouteEvent
import io.lb.middleware.shared.presentation.create_route.original_route.OriginalRouteViewModel
import io.middleware.api.domain.use_cases.TestOriginalRouteUseCase
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
