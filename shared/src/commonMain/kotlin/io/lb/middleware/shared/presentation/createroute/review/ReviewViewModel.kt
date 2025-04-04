package io.lb.middleware.shared.presentation.createroute.review

import io.lb.middleware.common.state.Resource
import io.lb.middleware.common.state.toCommonFlow
import io.lb.middleware.common.state.toCommonStateFlow
import io.middleware.api.domain.usecases.CreateNewRouteUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReviewViewModel(
    coroutineScope: CoroutineScope?,
    private val createNewRouteUseCase: CreateNewRouteUseCase,
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(ReviewState())
    val state = _state.toCommonStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow().toCommonFlow()

    sealed class UiEvent {
        data class ShowError(val message: String) : UiEvent()
        data class ShowCreatedRouteMessage(val result: String) : UiEvent()
    }

    fun onEvent(event: ReviewEvent) {
        when (event) {
            is ReviewEvent.CreateMappedRoute -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    createNewRouteUseCase(
                        path = event.mappedPath,
                        method = event.mappedMethod,
                        originalBaseUrl = event.originalBaseUrl,
                        originalPath = event.originalPath,
                        originalMethod = event.originalMethod,
                        originalQueries = event.originalQueries,
                        originalHeaders = event.originalHeaders,
                        originalBody = event.originalBody ?: "",
                        preConfiguredQueries = event.preConfiguredQueries,
                        preConfiguredHeaders = event.preConfiguredHeaders,
                        preConfiguredBody = event.preConfiguredBody,
                        newBodyFields = event.newBodyFields,
                        oldBodyFields = event.oldBodyFields,
                        ignoreEmptyValues = event.ignoreEmptyValues
                    ).collectLatest { result ->
                        when (result) {
                            is Resource.Success -> {
                                _eventFlow.emit(UiEvent.ShowCreatedRouteMessage(
                                    "/v1/" + (result.data ?: "") + "/" + event.mappedPath
                                ))
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
}
