package io.lb.middleware.impl.client.middleware.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
internal data class PreviewParameter(
    @SerialName("originalResponse")
    val originalResponse: JsonElement,
    @SerialName("mappingRules")
    val mappingRules: NewBodyMappingRuleParameter
)
