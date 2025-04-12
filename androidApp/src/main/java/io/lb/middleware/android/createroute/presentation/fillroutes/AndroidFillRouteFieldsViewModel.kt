package io.lb.middleware.android.createroute.presentation.fillroutes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lb.middleware.shared.presentation.createroute.fillpreconfigs.FillPreConfigsEvent
import io.lb.middleware.shared.presentation.createroute.fillpreconfigs.FillPreConfigsViewModel
import io.lb.middleware.shared.presentation.createroute.fillroutes.FillRouteFieldsEvent
import io.lb.middleware.shared.presentation.createroute.fillroutes.FillRouteFieldsViewModel
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
