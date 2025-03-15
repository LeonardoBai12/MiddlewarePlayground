package io.lb.middleware.impl.client.middleware.model

import io.lb.middleware.common.data.middleware.model.MappingRules
import io.lb.middleware.common.data.middleware.model.OriginalRoute
import io.lb.middleware.common.data.middleware.request.MiddlewareHttpMethods
import kotlinx.serialization.SerialName

internal data class MappingRequest(
    @SerialName("path")
    val path: String,
    @SerialName("method")
    val method: MiddlewareHttpMethods,
    @SerialName("originalRoute")
    val originalRoute: OriginalRoute,
    @SerialName("rulesAsString")
    val mappingRules: MappingRules
)
