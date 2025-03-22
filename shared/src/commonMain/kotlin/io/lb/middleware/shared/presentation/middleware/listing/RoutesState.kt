package io.lb.middleware.shared.presentation.middleware.listing

import io.lb.middleware.common.shared.middleware.model.MappedRoute

data class RoutesState(
    val routes: List<MappedRoute> = emptyList(),
    val apis: List<String> = emptyList(),
    val isLoading: Boolean = false,
)
