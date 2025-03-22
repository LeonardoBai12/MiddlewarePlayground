package io.lb.middleware.shared.presentation.create_route

import io.lb.middleware.common.shared.middleware.model.NewBodyField
import io.lb.middleware.common.shared.middleware.model.OldBodyField

data class CreateRouteState(
    val newBodyFields: Map<String, NewBodyField> = emptyMap(),
    val oldBodyFields: Map<String, OldBodyField> = emptyMap(),
    val originalQueries: Map<String, String> = emptyMap(),
    val originalHeaders: Map<String, String> = emptyMap(),
    val preConfiguredQueries: Map<String, String> = emptyMap(),
    val preConfiguredHeaders: Map<String, String> = emptyMap(),
    val ignoreEmptyValues: Boolean = true,
    val isLoading: Boolean = false,
)
