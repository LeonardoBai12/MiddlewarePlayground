package io.lb.middleware.shared.presentation.middleware.details

import io.lb.middleware.common.shared.middleware.model.MappedRoute

sealed  class RouteDetailsEvent {
    data class SaveRouteInHistory(val mappedRoute: MappedRoute) : RouteDetailsEvent()
    data object TestRoute : RouteDetailsEvent()
}
