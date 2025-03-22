package io.lb.middleware.shared.presentation.routes

sealed class RoutesEvent {
    data object GetRoutes : RoutesEvent()
}
