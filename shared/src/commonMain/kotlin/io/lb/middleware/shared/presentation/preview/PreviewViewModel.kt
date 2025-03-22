package io.lb.middleware.shared.presentation.preview

import io.lb.middleware.common.shared.middleware.model.NewBodyField
import io.lb.middleware.common.shared.middleware.model.OldBodyField
import io.lb.middleware.common.state.Resource
import io.lb.middleware.common.state.toCommonFlow
import io.lb.middleware.common.state.toCommonStateFlow
import io.middleware.api.domain.use_cases.RequestPreviewUseCase
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
            is PreviewEvent.UpsertNewBodyField -> {
                viewModelScope.launch {
                    val bodyFields = _state.value.newBodyFields.toMutableMap()
                    bodyFields[event.key] = event.field
                    _state.update {
                        it.copy(newBodyFields = bodyFields)
                    }
                }
            }
            is PreviewEvent.UpsertOldBodyField -> {
                viewModelScope.launch {
                    val bodyFields = _state.value.oldBodyFields.toMutableMap()
                    bodyFields[event.key] = event.field
                    _state.update {
                        it.copy(oldBodyFields = bodyFields)
                    }
                }
            }
            is PreviewEvent.RemoveNewBodyField -> {
                viewModelScope.launch {
                    val bodyFields = _state.value.newBodyFields.toMutableMap()
                    bodyFields.remove(event.key)
                    _state.update {
                        it.copy(newBodyFields = bodyFields)
                    }
                }
            }
            is PreviewEvent.RemoveOldBodyField -> {
                viewModelScope.launch {
                    val bodyFields = _state.value.oldBodyFields.toMutableMap()
                    bodyFields.remove(event.key)
                    _state.update {
                        it.copy(oldBodyFields = bodyFields)
                    }
                }
            }
            is PreviewEvent.UpdateOriginalResponse -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(originalResponse = event.response)
                    }
                }
            }
            PreviewEvent.RequestPreview -> {
                viewModelScope.launch {
                    runCatching {
                        requestPreview()
                    }.getOrElse {
                        _eventFlow.emit(UiEvent.ShowError(it.message ?: "An error occurred"))
                    }
                }
            }
        }
    }

    private suspend fun requestPreview() {
        requestPreviewUseCase(
            originalResponse = _state.value.originalResponse,
            newBodyFields = _state.value.newBodyFields,
            oldBodyFields = _state.value.oldBodyFields,
            ignoreEmptyValues = _state.value.ignoreEmptyValues,
        ).collectLatest { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        _eventFlow.emit(UiEvent.ShowResult(it))
                    } ?: run {
                        _eventFlow.emit(UiEvent.ShowError("An error occurred"))
                    }
                }
                is Resource.Error -> {
                    _eventFlow.emit(UiEvent.ShowError(result.throwable?.message ?: "An error occurred"))
                }
            }
        }
    }
}
