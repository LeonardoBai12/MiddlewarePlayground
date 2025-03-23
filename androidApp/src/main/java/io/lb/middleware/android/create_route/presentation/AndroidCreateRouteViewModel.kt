package io.lb.middleware.android.create_route.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lb.middleware.shared.presentation.create_route.CreateRouteEvent
import io.lb.middleware.shared.presentation.create_route.CreateRouteViewModel
import io.middleware.api.domain.use_cases.CreateNewRouteUseCase
import io.middleware.api.domain.use_cases.RequestPreviewUseCase
import io.middleware.api.domain.use_cases.TestOriginalRouteUseCase
import javax.inject.Inject

@HiltViewModel
class AndroidCreateRouteViewModel @Inject constructor(
    createNewRouteUseCase: CreateNewRouteUseCase,
    requestPreviewUseCase: RequestPreviewUseCase,
    testOriginalRouteUseCase: TestOriginalRouteUseCase,
) : ViewModel() {
    private val viewModel by lazy {
        CreateRouteViewModel(
            viewModelScope,
            createNewRouteUseCase,
            requestPreviewUseCase,
            testOriginalRouteUseCase,
        )
    }
    val state = viewModel.state
    val eventFlow = viewModel.eventFlow

    fun onEvent(event: CreateRouteEvent) {
        viewModel.onEvent(event)
    }
}
