package io.lb.middleware.shared.presentation.createroute.originalroute

data class OriginalRouteState(
    val originalQueries: Map<String, String> = emptyMap(),
    val originalHeaders: Map<String, String> = mapOf(
        "Content-Type" to "application/json",
    ),
    val isLoading: Boolean = false,
)
