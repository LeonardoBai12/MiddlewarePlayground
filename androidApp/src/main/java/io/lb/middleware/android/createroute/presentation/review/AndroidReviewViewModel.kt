package io.lb.middleware.android.createroute.presentation.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lb.middleware.shared.presentation.createroute.review.ReviewEvent
import io.lb.middleware.shared.presentation.createroute.review.ReviewViewModel
import io.middleware.api.domain.usecases.CreateNewRouteUseCase
import javax.inject.Inject

@HiltViewModel
class AndroidReviewViewModel @Inject constructor(
    createNewRouteUseCase: CreateNewRouteUseCase,
) : ViewModel() {
    private val viewModel by lazy {
        ReviewViewModel(
            viewModelScope,
            createNewRouteUseCase,
        )
    }
    val state = viewModel.state
    val eventFlow = viewModel.eventFlow

    fun onEvent(event: ReviewEvent) {
        viewModel.onEvent(event)
    }
}
