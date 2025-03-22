package io.lb.middleware.shared.presentation.history

import io.lb.middleware.common.state.toCommonFlow
import io.lb.middleware.common.state.toCommonStateFlow
import io.middleware.domain.history.use_cases.DeleteApiFromHistoryUseCase
import io.middleware.domain.history.use_cases.DeleteRouteFromHistoryUseCase
import io.middleware.domain.history.use_cases.GetApiHistoryUseCase
import io.middleware.domain.history.use_cases.GetRoutesByApiIdUseCase
import io.middleware.domain.history.use_cases.GetRoutesHistoryUseCase
import io.middleware.domain.history.use_cases.SwitchApiToFavouriteUseCase
import io.middleware.domain.history.use_cases.SwitchRouteToFavouriteUseCase
import io.middleware.domain.history.use_cases.WipeDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    coroutineScope: CoroutineScope?,
    val deleteApiFromHistoryUseCase: DeleteApiFromHistoryUseCase,
    val deleteRouteFromHistoryUseCase: DeleteRouteFromHistoryUseCase,
    val getApiHistoryUseCase: GetApiHistoryUseCase,
    val getRoutesByApiIdUseCase: GetRoutesByApiIdUseCase,
    val getRoutesHistoryUseCase: GetRoutesHistoryUseCase,
    val switchApiToFavouriteUseCase: SwitchApiToFavouriteUseCase,
    val switchRouteToFavouriteUseCase: SwitchRouteToFavouriteUseCase,
    val wipeDataUseCase: WipeDataUseCase,
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(HistoryState())
    val state = _state.toCommonStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow().toCommonFlow()

    sealed class UiEvent {
        data class ShowError(val message: String) : UiEvent()
        data object ShowDeletedMessage : UiEvent()
        data object ShowFavouriteMessage : UiEvent()
        data object ShowRemovedFromFavouriteMessage : UiEvent()
    }

    fun onEvent(event: HistoryEvent) {
        when (event) {
            HistoryEvent.ClearHistory -> {
                viewModelScope.launch {
                    wipeDataUseCase()
                    _state.update {
                        it.copy(
                            apiHistory = emptyList(),
                            routesHistory = emptyList()
                        )
                    }
                    _eventFlow.emit(UiEvent.ShowDeletedMessage)
                }
            }
            is HistoryEvent.DeleteApiFromHistory -> {
                viewModelScope.launch {
                    deleteApiFromHistoryUseCase(event.id)
                    _state.update {
                        it.copy(
                            apiHistory = it.apiHistory.filter { api -> api.uuid != event.id }
                        )
                    }
                    _eventFlow.emit(UiEvent.ShowDeletedMessage)
                }
            }
            HistoryEvent.GetApisHistory -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(isLoading = true)
                    }
                    val apiHistory = getApiHistoryUseCase()
                    _state.update {
                        it.copy(
                            apiHistory = apiHistory,
                            isLoading = false
                        )
                    }
                }
            }
            is HistoryEvent.GetRoutesByApi -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(isLoading = true)
                    }
                    val routes = getRoutesByApiIdUseCase(event.apiId)
                    _state.update {
                        it.copy(
                            routesHistory = routes,
                            isLoading = false
                        )
                    }
                }
            }
            HistoryEvent.GetRoutesHistory -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(isLoading = true)
                    }
                    val routes = getRoutesHistoryUseCase()
                    _state.update {
                        it.copy(
                            routesHistory = routes,
                            isLoading = false
                        )
                    }
                }
            }
            is HistoryEvent.SwitchApiToFavourite -> {
                viewModelScope.launch {
                    switchApiToFavouriteUseCase(event.id, event.isFavourite)
                    if (event.isFavourite) {
                        _eventFlow.emit(UiEvent.ShowFavouriteMessage)
                    } else {
                        _eventFlow.emit(UiEvent.ShowRemovedFromFavouriteMessage)
                    }
                }
            }
            is HistoryEvent.SwitchRouteToFavourite -> {
                viewModelScope.launch {
                    switchRouteToFavouriteUseCase(event.id, event.isFavourite)
                    if (event.isFavourite) {
                        _eventFlow.emit(UiEvent.ShowFavouriteMessage)
                    } else {
                        _eventFlow.emit(UiEvent.ShowRemovedFromFavouriteMessage)
                    }
                }
            }
            is HistoryEvent.DeleteRouteFromHistory -> {
                viewModelScope.launch {
                    deleteRouteFromHistoryUseCase(event.id)
                    _state.update {
                        it.copy(
                            routesHistory = it.routesHistory.filter { route ->
                                route.uuid != event.id
                            }
                        )
                    }
                    _eventFlow.emit(UiEvent.ShowDeletedMessage)
            }
        }
    }
}
