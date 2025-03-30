package io.lb.middleware.shared.presentation.createroute.fillroutes

import io.lb.middleware.common.state.toCommonFlow
import io.lb.middleware.common.state.toCommonStateFlow
import io.lb.middleware.shared.presentation.createroute.fillpreconfigs.FillPreConfigsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FillRouteFieldsViewModel(
    coroutineScope: CoroutineScope?,
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(FillRouteFieldsState())
    val state = _state.toCommonStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow().toCommonFlow()

    sealed class UiEvent {
        data class ShowError(val message: String) : UiEvent()
    }

    fun onEvent(event: FillRouteFieldsEvent) {
        when (event) {
            is FillRouteFieldsEvent.UpsertNewBodyField -> {
                viewModelScope.launch {
                    val bodyFields = _state.value.newBodyFields.toMutableMap()
                    bodyFields[event.key] = event.field
                    _state.update {
                        it.copy(newBodyFields = bodyFields)
                    }
                }
            }
            is FillRouteFieldsEvent.RemoveNewBodyField -> {
                viewModelScope.launch {
                    val bodyFields = _state.value.newBodyFields.toMutableMap()
                    bodyFields.remove(event.key)
                    _state.update {
                        it.copy(newBodyFields = bodyFields)
                    }
                }
            }
            is FillRouteFieldsEvent.UpsertOldBodyField -> {
                viewModelScope.launch {
                    val bodyFields = _state.value.oldBodyFields.toMutableMap()
                    bodyFields[event.key] = event.field
                    _state.update {
                        it.copy(oldBodyFields = bodyFields)
                    }
                }
            }
            is FillRouteFieldsEvent.RemoveOldBodyField -> {
                viewModelScope.launch {
                    val bodyFields = _state.value.oldBodyFields.toMutableMap()
                    bodyFields.remove(event.key)
                    _state.update {
                        it.copy(oldBodyFields = bodyFields)
                    }
                }
            }
        }
    }
}
