package io.lb.middleware.shared.presentation.create_route

import io.lb.middleware.common.state.toCommonFlow
import io.lb.middleware.common.state.toCommonStateFlow
import io.middleware.api.domain.use_cases.CreateNewRouteUseCase
import io.middleware.api.domain.use_cases.GetAllRoutesUseCase
import io.middleware.api.domain.use_cases.RequestPreviewUseCase
import io.middleware.api.domain.use_cases.TestOriginalRouteUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow

class CreateRouteViewModel(
    coroutineScope: CoroutineScope?,
    private val createNewRouteUseCase: CreateNewRouteUseCase,
    private val previewUseCase: RequestPreviewUseCase,
    private val testOriginalRouteUseCase: TestOriginalRouteUseCase,
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(CreateRouteState())
    val state = _state.toCommonStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow().toCommonFlow()

    sealed class UiEvent {
        data class ShowError(val message: String) : UiEvent()
        data object ShowCreatedRouteMessage : UiEvent()
        data object ShowPreviewMessage : UiEvent()
    }

    fun onEvent(event: CreateRouteEvent) {
        when (event) {
            is CreateRouteEvent.RemoveNewBodyField -> {

            }
            is CreateRouteEvent.RemoveOldBodyField -> {

            }
            CreateRouteEvent.RequestPreview -> {

            }
            is CreateRouteEvent.TestOriginalRoute -> {

            }
            is CreateRouteEvent.UpdateOriginalResponse -> {

            }
            is CreateRouteEvent.UpsertNewBodyField -> {

            }
            is CreateRouteEvent.UpsertOldBodyField -> {

            }
        }
    }
}
