package io.lb.middleware.impl.client.middleware.remote.model

import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
internal data class MappedRouteResponse(
    @SerialName("uuid")
    val uuid: String,
    @SerialName("path")
    val path: String,
    @SerialName("originalRoute")
    val originalRoute: OriginalRouteParameter,
    @SerialName("method")
    val method: MiddlewareHttpMethods,
    @SerialName("mappedApi")
    val mappedApi: MappedApiParameter,
    @SerialName("preConfiguredQueries")
    val preConfiguredQueries: Map<String, String> = mapOf(),
    @SerialName("preConfiguredHeaders")
    val preConfiguredHeaders: Map<String, String> = mapOf(),
    @SerialName("preConfiguredBody")
    val preConfiguredBody: JsonObject? = null,
    @SerialName("rulesAsString")
    val rulesAsString: NewBodyMappingRuleParameter?
)
