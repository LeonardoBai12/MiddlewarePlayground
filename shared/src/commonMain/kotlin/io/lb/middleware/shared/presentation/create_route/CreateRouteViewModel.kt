package io.lb.middleware.shared.presentation.create_route

import io.lb.middleware.common.state.Resource
import io.lb.middleware.common.state.toCommonFlow
import io.lb.middleware.common.state.toCommonStateFlow
import io.middleware.api.domain.use_cases.CreateNewRouteUseCase
import io.middleware.api.domain.use_cases.RequestPreviewUseCase
import io.middleware.api.domain.use_cases.TestOriginalRouteUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateRouteViewModel(
    coroutineScope: CoroutineScope?,
    private val createNewRouteUseCase: CreateNewRouteUseCase,
    private val requestPreviewUseCase: RequestPreviewUseCase,
    private val testOriginalRouteUseCase: TestOriginalRouteUseCase,
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(CreateRouteState())
    val state = _state.toCommonStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow().toCommonFlow()

    sealed class UiEvent {
        data class ShowError(val message: String) : UiEvent()
        data object ShowOriginalRouteSuccess : UiEvent()
        data object ShowCreatedRouteMessage : UiEvent()
        data class ShowPreview(val response: String) : UiEvent()
    }

    fun onEvent(event: CreateRouteEvent) {
        when (event) {
            is CreateRouteEvent.UpsertNewBodyField -> {
                viewModelScope.launch {
                    val bodyFields = _state.value.newBodyFields.toMutableMap()
                    bodyFields[event.key] = event.field
                    _state.update {
                        it.copy(newBodyFields = bodyFields)
                    }
                }
            }
            is CreateRouteEvent.RemoveNewBodyField -> {
                viewModelScope.launch {
                    val bodyFields = _state.value.newBodyFields.toMutableMap()
                    bodyFields.remove(event.key)
                    _state.update {
                        it.copy(newBodyFields = bodyFields)
                    }
                }
            }
            is CreateRouteEvent.UpsertOldBodyField -> {
                viewModelScope.launch {
                    val bodyFields = _state.value.oldBodyFields.toMutableMap()
                    bodyFields[event.key] = event.field
                    _state.update {
                        it.copy(oldBodyFields = bodyFields)
                    }
                }
            }
            is CreateRouteEvent.RemoveOldBodyField -> {
                viewModelScope.launch {
                    val bodyFields = _state.value.oldBodyFields.toMutableMap()
                    bodyFields.remove(event.key)
                    _state.update {
                        it.copy(oldBodyFields = bodyFields)
                    }
                }
            }
            is CreateRouteEvent.RequestPreview -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    val result = kotlin.runCatching {
                        testOriginalRouteUseCase(
                            originalBaseUrl = event.originalBaseUrl,
                            originalPath = event.originalPath,
                            originalMethod = event.originalMethod,
                            originalQueries = _state.value.originalQueries,
                            originalHeaders = _state.value.originalHeaders,
                            originalBody = event.originalBody,
                        )
                    }.getOrElse { error ->
                        _eventFlow.emit(UiEvent.ShowError(error.message ?: "Something went wrong"))
                        _state.update { it.copy(isLoading = false) }
                        return@launch
                    }

                    requestPreview(response = result ?: "")
                }
            }
            is CreateRouteEvent.TestOriginalRoute -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    kotlin.runCatching {
                        val result = testOriginalRouteUseCase(
                            originalBaseUrl = event.originalBaseUrl,
                            originalPath = event.originalPath,
                            originalMethod = event.originalMethod,
                            originalQueries = _state.value.originalQueries,
                            originalHeaders = _state.value.originalHeaders,
                            originalBody = event.originalBody,
                        )
                        result?.let {
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
            is CreateRouteEvent.UpsertOriginalQuery -> {
                viewModelScope.launch {
                    val queries = _state.value.originalQueries.toMutableMap()
                    queries[event.key] = event.value
                    _state.update {
                        it.copy(originalQueries = queries)
                    }
                }
            }
            is CreateRouteEvent.RemoveOriginalQuery -> {
                viewModelScope.launch {
                    val queries = _state.value.originalQueries.toMutableMap()
                    queries.remove(event.key)
                    _state.update {
                        it.copy(originalQueries = queries)
                    }
                }
            }
            is CreateRouteEvent.UpsertOriginalHeader -> {
                viewModelScope.launch {
                    val headers = _state.value.originalHeaders.toMutableMap()
                    headers[event.key] = event.value
                    _state.update {
                        it.copy(originalHeaders = headers)
                    }
                }
            }
            is CreateRouteEvent.RemoveOriginalHeader -> {
                viewModelScope.launch {
                    val headers = _state.value.originalHeaders.toMutableMap()
                    headers.remove(event.key)
                    _state.update {
                        it.copy(originalHeaders = headers)
                    }
                }
            }
            is CreateRouteEvent.UpsertPreConfiguredHeader -> {
                viewModelScope.launch {
                    val headers = _state.value.preConfiguredHeaders.toMutableMap()
                    headers[event.key] = event.value
                    _state.update {
                        it.copy(preConfiguredHeaders = headers)
                    }
                }
            }
            is CreateRouteEvent.RemovePreConfiguredHeader -> {
                viewModelScope.launch {
                    val headers = _state.value.preConfiguredHeaders.toMutableMap()
                    headers.remove(event.key)
                    _state.update {
                        it.copy(preConfiguredHeaders = headers)
                    }
                }
            }
            is CreateRouteEvent.UpsertPreConfiguredQuery -> {
                viewModelScope.launch {
                    val queries = _state.value.preConfiguredQueries.toMutableMap()
                    queries[event.key] = event.value
                    _state.update {
                        it.copy(preConfiguredQueries = queries)
                    }
                }
            }
            is CreateRouteEvent.RemovePreConfiguredQuery -> {
                viewModelScope.launch {
                    val queries = _state.value.preConfiguredQueries.toMutableMap()
                    queries.remove(event.key)
                    _state.update {
                        it.copy(preConfiguredQueries = queries)
                    }
                }
            }

            is CreateRouteEvent.CreateMappedRoute -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    createNewRouteUseCase(
                        path = event.mappedPath,
                        method = event.mappedMethod,
                        originalBaseUrl = event.originalBaseUrl,
                        originalPath = event.originalPath,
                        originalMethod = event.originalMethod,
                        originalQueries = _state.value.originalQueries,
                        originalHeaders = _state.value.originalHeaders,
                        originalBody = event.originalBody ?: "",
                        preConfiguredQueries = _state.value.preConfiguredQueries,
                        preConfiguredHeaders = _state.value.preConfiguredHeaders,
                        preConfiguredBody = event.preConfiguredBody,
                        newBodyFields = _state.value.newBodyFields,
                        oldBodyFields = _state.value.oldBodyFields,
                        ignoreEmptyValues = _state.value.ignoreEmptyValues
                    ).collectLatest { result ->
                        when (result) {
                            is Resource.Success -> {
                                _eventFlow.emit(UiEvent.ShowCreatedRouteMessage)
                                _state.update { it.copy(isLoading = false) }
                            }

                            is Resource.Error -> {
                                _eventFlow.emit(
                                    UiEvent.ShowError(
                                        result.throwable?.message ?: "Something went wrong"
                                    )
                                )
                                _state.update { it.copy(isLoading = false) }
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun requestPreview(response: String) {
        requestPreviewUseCase(
            originalResponse = response,
            newBodyFields = _state.value.newBodyFields,
            oldBodyFields = _state.value.oldBodyFields,
            ignoreEmptyValues = _state.value.ignoreEmptyValues,
        ).collectLatest { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        _eventFlow.emit(UiEvent.ShowPreview(it))
                        _state.update { it.copy(isLoading = false) }
                    } ?: run {
                        _eventFlow.emit(UiEvent.ShowError("Something went wrong"))
                        _state.update { it.copy(isLoading = false) }
                    }
                }
                is Resource.Error -> {
                    _eventFlow.emit(UiEvent.ShowError(result.throwable?.message ?: "Something went wrong"))
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }
}
