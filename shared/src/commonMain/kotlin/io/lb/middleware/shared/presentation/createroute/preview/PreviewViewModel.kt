package io.lb.middleware.shared.presentation.createroute.preview

import io.lb.middleware.common.shared.middleware.model.NewBodyField
import io.lb.middleware.common.shared.middleware.model.OldBodyField
import io.lb.middleware.common.state.Resource
import io.lb.middleware.common.state.toCommonFlow
import io.lb.middleware.common.state.toCommonStateFlow
import io.middleware.api.domain.usecases.RequestPreviewUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PreviewViewModel(
    coroutineScope: CoroutineScope?,
    private val requestPreviewUseCase: RequestPreviewUseCase
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(PreviewState())
    val state = _state.toCommonStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow().toCommonFlow()

    sealed class UiEvent {
        data class ShowError(val message: String) : UiEvent()
        data class ShowResult(val result: String) : UiEvent()
    }

    fun onEvent(event: PreviewEvent) {
        when (event) {
            is PreviewEvent.RequestPreview -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    runCatching {
                        requestPreview(
                            event.response,
                            event.newBodyFields,
                            event.oldBodyFields,
                            event.ignoreEmptyValues
                        )
                    }.getOrElse { error ->
                        _eventFlow.emit(UiEvent.ShowError(error.message ?: "Something went wrong"))
                        _state.update { it.copy(isLoading = false) }
                    }
                }
            }
        }
    }

    private suspend fun requestPreview(
        response: String,
        newBodyFields: Map<String, NewBodyField>,
        oldBodyFields: Map<String, OldBodyField>,
        ignoreEmptyValues: Boolean
    ) {
        requestPreviewUseCase(
            originalResponse = response,
            newBodyFields = newBodyFields,
            oldBodyFields = oldBodyFields,
            ignoreEmptyValues = ignoreEmptyValues,
        ).collectLatest { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        _eventFlow.emit(UiEvent.ShowResult(it))
                    } ?: run {
                        _eventFlow.emit(UiEvent.ShowError("Something went wrong"))
                    }
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
