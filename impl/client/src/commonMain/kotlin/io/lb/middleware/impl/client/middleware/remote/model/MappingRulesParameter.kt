package io.lb.middleware.impl.client.middleware.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MappingRulesParameter(
    @SerialName("mappingRules")
    val mappingRules: NewBodyMappingRuleParameter,
)
