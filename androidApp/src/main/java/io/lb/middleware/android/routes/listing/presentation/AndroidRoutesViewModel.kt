package io.lb.middleware.android.routes.listing.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lb.middleware.shared.presentation.middleware.listing.RoutesEvent
import io.lb.middleware.shared.presentation.middleware.listing.RoutesViewModel
import io.middleware.api.domain.use_cases.GetAllRoutesUseCase
import javax.inject.Inject

@HiltViewModel
class AndroidRoutesViewModel @Inject constructor(
    private val getAllRoutesUseCase: GetAllRoutesUseCase
) : ViewModel() {
    private val viewModel by lazy {
        RoutesViewModel(
            viewModelScope,
            getAllRoutesUseCase,
        )
    }
    val state = viewModel.state
    val eventFlow = viewModel.eventFlow

    fun onEvent(event: RoutesEvent) {
        viewModel.onEvent(event)
    }
}
