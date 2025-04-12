package io.lb.middleware.android.createroute.presentation.fillpreconfigs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lb.middleware.shared.presentation.createroute.fillpreconfigs.FillPreConfigsEvent
import io.lb.middleware.shared.presentation.createroute.fillpreconfigs.FillPreConfigsViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidFillPreConfigsViewModel @Inject constructor() : ViewModel() {
    private val viewModel by lazy {
        FillPreConfigsViewModel(viewModelScope)
    }
    val state = viewModel.state
    val eventFlow = viewModel.eventFlow

    fun onEvent(event: FillPreConfigsEvent) {
        viewModel.onEvent(event)
    }
}
