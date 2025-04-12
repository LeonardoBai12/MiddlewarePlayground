package io.lb.middleware.shared.presentation.createroute.fillpreconfigs

data class FillPreConfigsState(
    val preConfiguredQueries: Map<String, String> = emptyMap(),
    val preConfiguredHeaders: Map<String, String> = mapOf(
        "Content-Type" to "application/json",
    ),
    val ignoreEmptyValues: Boolean = true,
)
