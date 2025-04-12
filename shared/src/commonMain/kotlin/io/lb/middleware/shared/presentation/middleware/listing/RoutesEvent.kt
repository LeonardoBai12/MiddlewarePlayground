package io.lb.middleware.shared.presentation.middleware.listing

sealed class RoutesEvent {
    data object GetRoutes : RoutesEvent()
}
