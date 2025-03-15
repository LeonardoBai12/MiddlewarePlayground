package io.lb.middleware.common.shared.middleware.model

import io.lb.middleware.common.data.middleware.remote.model.NewBodyField

data class MappingRules(
    val newBodyFields: Map<String, NewBodyField>,
    val oldBodyFields: Map<String, OldBodyField>,
    val ignoreEmptyValues: Boolean = false
)
