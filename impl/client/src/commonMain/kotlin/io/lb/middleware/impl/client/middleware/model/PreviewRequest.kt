package io.lb.middleware.impl.client.middleware.model

import io.lb.middleware.common.data.middleware.model.MappingRules
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PreviewRequest(
    @SerialName("originalResponse")
    val originalResponse: String,
    @SerialName("mappingRules")
    val mappingRules: MappingRules
)
