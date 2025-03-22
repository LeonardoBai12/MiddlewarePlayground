package io.lb.middleware.shared.presentation.routes

import io.lb.middleware.common.shared.middleware.model.MappedRoute

data class RoutesState(
    val routes: List<MappedRoute> = emptyList(),
    val apis: List<String> = emptyList()
)
