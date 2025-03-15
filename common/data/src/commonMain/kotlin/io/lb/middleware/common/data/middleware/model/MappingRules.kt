package io.lb.middleware.common.data.middleware.model

import io.lb.middleware.mapper.model.NewBodyMappingRule
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MappingRules(
    @SerialName("mappingRules")
    val mappingRules: NewBodyMappingRule,
)
