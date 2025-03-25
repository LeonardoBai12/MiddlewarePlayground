package io.lb.middleware.android.createroute.presentation.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lb.middleware.shared.presentation.createroute.preview.PreviewEvent
import io.lb.middleware.shared.presentation.createroute.preview.PreviewViewModel
import io.middleware.api.domain.usecases.RequestPreviewUseCase
import javax.inject.Inject

@HiltViewModel
class AndroidPreviewViewModel @Inject constructor(
    previewUseCase: RequestPreviewUseCase
) : ViewModel() {
    private val viewModel by lazy {
        PreviewViewModel(
            viewModelScope,
            previewUseCase,
        )
    }
    val state = viewModel.state
    val eventFlow = viewModel.eventFlow

    fun onEvent(event: PreviewEvent) {
        viewModel.onEvent(event)
    }
}
