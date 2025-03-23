package io.lb.middleware.shared.presentation.middleware.details

import io.lb.middleware.common.shared.middleware.model.MappedRoute

data class RouteDetailsState(
    val mappedRoute: MappedRoute? = null,
    val isLoading: Boolean = true,
)
