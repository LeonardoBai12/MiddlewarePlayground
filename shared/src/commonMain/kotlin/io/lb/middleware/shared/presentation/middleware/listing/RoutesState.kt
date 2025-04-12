package io.lb.middleware.shared.presentation.middleware.listing

import io.lb.middleware.common.shared.middleware.model.MappedRoute

data class RoutesState(
    val routes: List<MappedRoute> = emptyList(),
    val apis: Map<String, List<MappedRoute>> = emptyMap(),
    val isLoading: Boolean = true,
)
