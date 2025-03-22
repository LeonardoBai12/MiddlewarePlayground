package io.lb.middleware.shared.presentation.history

import io.lb.middleware.common.shared.middleware.model.MappedApi
import io.lb.middleware.common.shared.middleware.model.MappedRoute

data class HistoryState(
    val apiHistory: List<MappedApi> = emptyList(),
    val routesHistory: List<MappedRoute> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
)
