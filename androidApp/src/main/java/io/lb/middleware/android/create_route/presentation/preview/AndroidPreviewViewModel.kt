package io.lb.middleware.android.create_route.presentation.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lb.middleware.shared.presentation.create_route.preview.PreviewEvent
import io.lb.middleware.shared.presentation.create_route.preview.PreviewViewModel
import io.middleware.api.domain.use_cases.RequestPreviewUseCase
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
