package io.lb.middleware.shared.presentation.createroute.originalroute

import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods
import io.lb.middleware.common.shared.util.beautifyJson
import io.lb.middleware.common.state.toCommonFlow
import io.lb.middleware.common.state.toCommonStateFlow
import io.middleware.api.domain.usecases.TestOriginalRouteUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OriginalRouteViewModel(
    coroutineScope: CoroutineScope?,
    private val testOriginalRouteUseCase: TestOriginalRouteUseCase,
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(OriginalRouteState())
    val state = _state.toCommonStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow().toCommonFlow()

    sealed class UiEvent {
        data class ShowError(val message: String) : UiEvent()
        data object ShowOriginalRouteSuccess : UiEvent()
        data class ShowResult(val code: Int, val result: String) : UiEvent()
        data object NavigateToNextStep : UiEvent()
    }

    fun onEvent(event: OriginalRouteEvent) {
        when (event) {
            is OriginalRouteEvent.TestOriginalRoute -> {
                viewModelScope.launch {
                    testOriginalRoute(
                        originalBaseUrl = event.originalBaseUrl,
                        originalPath = event.originalPath,
                        originalMethod = event.originalMethod,
                        originalBody = event.originalBody,
                    )
                }
            }
            is OriginalRouteEvent.UpsertOriginalQuery -> {
                viewModelScope.launch {
                    val queries = _state.value.originalQueries.toMutableMap()
                    queries[event.key] = event.value
                    _state.update {
                        it.copy(originalQueries = queries)
                    }
                }
            }
            is OriginalRouteEvent.RemoveOriginalQuery -> {
                viewModelScope.launch {
                    val queries = _state.value.originalQueries.toMutableMap()
                    queries.remove(event.key)
                    _state.update {
                        it.copy(originalQueries = queries)
                    }
                }
            }
            is OriginalRouteEvent.UpsertOriginalHeader -> {
                viewModelScope.launch {
                    val headers = _state.value.originalHeaders.toMutableMap()
                    headers[event.key] = event.value
                    _state.update {
                        it.copy(originalHeaders = headers)
                    }
                }
            }
            is OriginalRouteEvent.RemoveOriginalHeader -> {
                viewModelScope.launch {
                    val headers = _state.value.originalHeaders.toMutableMap()
                    headers.remove(event.key)
                    _state.update {
                        it.copy(originalHeaders = headers)
                    }
                }
            }
            is OriginalRouteEvent.MoveForward -> {
                viewModelScope.launch {
                    if (event.result.isBlank()) {
                        _eventFlow.emit(UiEvent.ShowError("Test the route before moving forward"))
                        return@launch
                    }
                    val isSuccess = event.code in 200..299
                    if (event.result.isNotBlank() && isSuccess.not()) {
                        _eventFlow.emit(UiEvent.ShowError("Fix the route before moving forward"))
                        return@launch
                    }
                    _eventFlow.emit(UiEvent.NavigateToNextStep)
                }
            }
        }
    }

    private suspend fun testOriginalRoute(
        originalBaseUrl: String,
        originalPath: String,
        originalMethod: MiddlewareHttpMethods,
        originalBody: String?,
    ) {
        _state.update { it.copy(isLoading = true) }
        kotlin.runCatching {
            val result = testOriginalRouteUseCase(
                originalBaseUrl = originalBaseUrl,
                originalPath = originalPath,
                originalMethod = originalMethod,
                originalQueries = _state.value.originalQueries,
                originalHeaders = _state.value.originalHeaders,
                originalBody = originalBody,
            )
            val isSuccess = result.first in 200..299
            result.second?.let {
                if (isSuccess) {
                    val json = beautifyJson(it)
                    _eventFlow.emit(UiEvent.ShowResult(result.first, json))
                } else {
                    _eventFlow.emit(UiEvent.ShowError(it))
                }
                _eventFlow.emit(UiEvent.ShowOriginalRouteSuccess)
                _state.update { it.copy(isLoading = false) }
            } ?: run {
                _eventFlow.emit(UiEvent.ShowError("Something went wrong"))
                _state.update { it.copy(isLoading = false) }
            }
        }.getOrElse { error ->
            _eventFlow.emit(UiEvent.ShowError(error.message ?: "Something went wrong"))
            _state.update { it.copy(isLoading = false) }
        }
    }
}
