package io.lb.middleware.shared.presentation.middleware.details

import io.lb.middleware.common.state.toCommonFlow
import io.lb.middleware.common.state.toCommonStateFlow
import io.middleware.api.domain.use_cases.RequestMappedRouteUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RouteDetailsViewModel(
    coroutineScope: CoroutineScope?,
    private val requestMappedRouteUseCase: RequestMappedRouteUseCase
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(RouteDetailsState())
    val state = _state.toCommonStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow().toCommonFlow()

    sealed class UiEvent {
        data class ShowError(val message: String) : UiEvent()
        data class ShowResult(val result: String) : UiEvent()
    }

    fun onEvent(event: RouteDetailsEvent) {
        when (event) {
            is RouteDetailsEvent.TestRoute -> {
                viewModelScope.launch {
                    testRoute()
                }
            }
        }
    }

    private suspend fun testRoute() {
        _state.value.mappedRoute?.let {
            runCatching {
                _state.update { it.copy(isLoading = true) }
                requestMappedRouteUseCase.invoke(
                    path = it.path,
                    method = it.method,
                    queries = emptyMap(),
                    preConfiguredQueries = it.preConfiguredQueries,
                    preConfiguredHeaders = it.preConfiguredHeaders,
                    preConfiguredBody = it.preConfiguredBody
                )?.let { result ->
                    _eventFlow.emit(UiEvent.ShowResult(result))
                    _state.update { it.copy(isLoading = false) }
                } ?: run {
                    _eventFlow.emit(UiEvent.ShowError("Failed to test route"))
                    _state.update { it.copy(isLoading = false) }
                }
            }.getOrElse {
                _eventFlow.emit(UiEvent.ShowError(it.message ?: "Failed to test route"))
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}
