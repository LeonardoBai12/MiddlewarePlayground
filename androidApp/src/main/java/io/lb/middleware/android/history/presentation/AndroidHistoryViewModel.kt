package io.lb.middleware.android.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lb.middleware.shared.presentation.history.HistoryEvent
import io.lb.middleware.shared.presentation.history.HistoryViewModel
import io.middleware.domain.history.use_cases.DeleteApiFromHistoryUseCase
import io.middleware.domain.history.use_cases.DeleteRouteFromHistoryUseCase
import io.middleware.domain.history.use_cases.GetApiHistoryUseCase
import io.middleware.domain.history.use_cases.GetRoutesByApiIdUseCase
import io.middleware.domain.history.use_cases.GetRoutesHistoryUseCase
import io.middleware.domain.history.use_cases.SwitchApiToFavouriteUseCase
import io.middleware.domain.history.use_cases.SwitchRouteToFavouriteUseCase
import io.middleware.domain.history.use_cases.WipeDataUseCase
import javax.inject.Inject

@HiltViewModel
class AndroidHistoryViewModel @Inject constructor(
    deleteApiFromHistoryUseCase: DeleteApiFromHistoryUseCase,
    deleteRouteFromHistoryUseCase: DeleteRouteFromHistoryUseCase,
    getApiHistoryUseCase: GetApiHistoryUseCase,
    getRoutesByApiIdUseCase: GetRoutesByApiIdUseCase,
    getRoutesHistoryUseCase: GetRoutesHistoryUseCase,
    switchApiToFavouriteUseCase: SwitchApiToFavouriteUseCase,
    switchRouteToFavouriteUseCase: SwitchRouteToFavouriteUseCase,
    wipeDataUseCase: WipeDataUseCase,
) : ViewModel() {
    private val viewModel by lazy {
        HistoryViewModel(
            viewModelScope,
            deleteApiFromHistoryUseCase,
            deleteRouteFromHistoryUseCase,
            getApiHistoryUseCase,
            getRoutesByApiIdUseCase,
            getRoutesHistoryUseCase,
            switchApiToFavouriteUseCase,
            switchRouteToFavouriteUseCase,
            wipeDataUseCase,
        )
    }
    val state = viewModel.state
    val eventFlow = viewModel.eventFlow

    fun onEvent(event: HistoryEvent) {
        viewModel.onEvent(event)
    }
}
