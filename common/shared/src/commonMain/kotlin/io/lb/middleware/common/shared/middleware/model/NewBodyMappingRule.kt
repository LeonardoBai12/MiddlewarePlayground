package io.lb.middleware.common.shared.middleware.model

import io.lb.middleware.common.data.middleware.remote.model.NewBodyField

/**
 * Represents a new body mapping rule.
 *
 * @property newBodyFields The new body fields.
 * @property oldBodyFields The old body fields.
 * @property ignoreEmptyValues Whether to ignore empty values.
 */
data class NewBodyMappingRule(
    val newBodyFields: Map<String, NewBodyField>,
    val oldBodyFields: Map<String, OldBodyField>,
    val ignoreEmptyValues: Boolean = false
)
