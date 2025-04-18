package io.lb.middleware.android.routes.details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lb.middleware.shared.presentation.middleware.details.RouteDetailsEvent
import io.lb.middleware.shared.presentation.middleware.details.RouteDetailsViewModel
import io.middleware.api.domain.usecases.RequestMappedRouteUseCase
import io.middleware.api.domain.usecases.SaveRouteInHistoryUseCase
import javax.inject.Inject

@HiltViewModel
class AndroidRouteDetailsViewModel @Inject constructor(
    requestMappedRouteUseCase: RequestMappedRouteUseCase,
    saveRouteInHistoryUseCase: SaveRouteInHistoryUseCase
) : ViewModel() {
    private val viewModel by lazy {
        RouteDetailsViewModel(
            viewModelScope,
            requestMappedRouteUseCase,
            saveRouteInHistoryUseCase
        )
    }
    val state = viewModel.state
    val eventFlow = viewModel.eventFlow

    fun onEvent(event: RouteDetailsEvent) {
        viewModel.onEvent(event)
    }
}
