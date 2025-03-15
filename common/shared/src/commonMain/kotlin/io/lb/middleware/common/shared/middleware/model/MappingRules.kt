package io.lb.middleware.common.shared.middleware.model

data class MappingRules(
    val newBodyFields: Map<String, NewBodyField>,
    val oldBodyFields: Map<String, OldBodyField>,
    val ignoreEmptyValues: Boolean = false
)
