package io.lb.middleware.shared.presentation.route_details

sealed  class RouteDetailsEvent {
    data object TestRoute : RouteDetailsEvent()
}
