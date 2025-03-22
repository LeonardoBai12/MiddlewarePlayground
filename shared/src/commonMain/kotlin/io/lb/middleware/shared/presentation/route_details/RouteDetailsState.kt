package io.lb.middleware.shared.presentation.route_details

import io.lb.middleware.common.shared.middleware.model.MappedRoute

data class RouteDetailsState(
    val mappedRoute: MappedRoute? = null,
    val isLoading: Boolean = false,
)
