package io.lb.middleware.shared.presentation.createroute.fillpreconfigs

import io.lb.middleware.common.state.toCommonFlow
import io.lb.middleware.common.state.toCommonStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FillPreConfigsViewModel(
    coroutineScope: CoroutineScope?,
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(FillPreConfigsState())
    val state = _state.toCommonStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow().toCommonFlow()

    sealed class UiEvent {
        data class ShowError(val message: String) : UiEvent()
    }

    fun onEvent(event: FillPreConfigsEvent) {
        when (event) {
            is FillPreConfigsEvent.UpsertPreConfiguredHeader -> {
                viewModelScope.launch {
                    val headers = _state.value.preConfiguredHeaders.toMutableMap()
                    headers[event.key] = event.value
                    _state.update {
                        it.copy(preConfiguredHeaders = headers)
                    }
                }
            }
            is FillPreConfigsEvent.RemovePreConfiguredHeader -> {
                viewModelScope.launch {
                    val headers = _state.value.preConfiguredHeaders.toMutableMap()
                    headers.remove(event.key)
                    _state.update {
                        it.copy(preConfiguredHeaders = headers)
                    }
                }
            }
            is FillPreConfigsEvent.UpsertPreConfiguredQuery -> {
                viewModelScope.launch {
                    val queries = _state.value.preConfiguredQueries.toMutableMap()
                    queries[event.key] = event.value
                    _state.update {
                        it.copy(preConfiguredQueries = queries)
                    }
                }
            }
            is FillPreConfigsEvent.RemovePreConfiguredQuery -> {
                viewModelScope.launch {
                    val queries = _state.value.preConfiguredQueries.toMutableMap()
                    queries.remove(event.key)
                    _state.update {
                        it.copy(preConfiguredQueries = queries)
                    }
                }
            }
        }
    }
}
