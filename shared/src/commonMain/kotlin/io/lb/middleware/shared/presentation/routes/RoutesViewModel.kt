package io.lb.middleware.shared.presentation.routes

import io.lb.middleware.common.state.Resource
import io.lb.middleware.common.state.toCommonFlow
import io.lb.middleware.common.state.toCommonStateFlow
import io.middleware.api.domain.use_cases.GetAllRoutesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RoutesViewModel(
    coroutineScope: CoroutineScope?,
    private val getAllRoutesUseCase: GetAllRoutesUseCase,
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(RoutesState())
    val state = _state.toCommonStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow().toCommonFlow()

    sealed class UiEvent {
        data class ShowError(val message: String) : UiEvent()
    }

    fun onEvent(event: RoutesEvent) {
        when (event) {
            RoutesEvent.GetRoutes -> {
                viewModelScope.launch {
                    getAllRoutes()
                }
            }
        }
    }

    private suspend fun RoutesViewModel.getAllRoutes() {
        _state.update { it.copy(isLoading = true) }
        getAllRoutesUseCase().collectLatest { result ->
            when (result) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            routes = result.data ?: emptyList(),
                            apis = result.data?.map { route ->
                                route.originalBaseUrl
                            } ?: emptyList(),
                            isLoading = false
                        )
                    }
                }

                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _eventFlow.emit(
                        UiEvent.ShowError(
                            result.throwable?.message ?: "Something went wrong"
                        )
                    )
                }
            }
        }
    }
}
