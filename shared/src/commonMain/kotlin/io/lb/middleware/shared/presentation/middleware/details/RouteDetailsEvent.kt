package io.lb.middleware.shared.presentation.middleware.details

sealed  class RouteDetailsEvent {
    data object TestRoute : RouteDetailsEvent()
}
