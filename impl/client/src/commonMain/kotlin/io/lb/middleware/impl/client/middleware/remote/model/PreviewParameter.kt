package io.lb.middleware.impl.client.middleware.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PreviewParameter(
    @SerialName("originalResponse")
    val originalResponse: String,
    @SerialName("mappingRules")
    val mappingRules: NewBodyMappingRuleParameter
)
