package io.lb.middleware.android.createroute.presentation.fillroutes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lb.middleware.shared.presentation.createroute.fill_routes.FillRouteFieldsEvent
import io.lb.middleware.shared.presentation.createroute.fill_routes.FillRouteFieldsViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidFillRouteFieldsViewModel @Inject constructor() : ViewModel() {
    private val viewModel by lazy {
        FillRouteFieldsViewModel(viewModelScope)
    }
    val state = viewModel.state
    val eventFlow = viewModel.eventFlow

    fun onEvent(event: FillRouteFieldsEvent) {
        viewModel.onEvent(event)
    }
}
