package io.lb.middleware.shared.presentation.preview

import io.lb.middleware.common.shared.middleware.model.NewBodyField
import io.lb.middleware.common.shared.middleware.model.OldBodyField

data class PreviewState(
    val newBodyFields: Map<String, NewBodyField> = emptyMap(),
    val oldBodyFields: Map<String, OldBodyField> = emptyMap(),
    val ignoreEmptyValues: Boolean = true,
    val isLoading: Boolean = false,
)
