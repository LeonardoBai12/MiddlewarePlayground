package io.lb.middleware.shared.presentation.create_route.original_route

data class OriginalRouteState(
    val originalQueries: Map<String, String> = emptyMap(),
    val originalHeaders: Map<String, String> = emptyMap(),
    val isLoading: Boolean = true,
)
