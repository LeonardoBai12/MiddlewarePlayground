package io.lb.middleware.shared.presentation.createroute.fill_routes

import io.lb.middleware.common.shared.middleware.model.NewBodyField
import io.lb.middleware.common.shared.middleware.model.OldBodyField

data class FillRouteFieldsState(
    val newBodyFields: Map<String, NewBodyField> = emptyMap(),
    val oldBodyFields: Map<String, OldBodyField> = emptyMap(),
    val preConfiguredQueries: Map<String, String> = emptyMap(),
    val preConfiguredHeaders: Map<String, String> = emptyMap(),
    val ignoreEmptyValues: Boolean = true,
    val isLoading: Boolean = false,
)
