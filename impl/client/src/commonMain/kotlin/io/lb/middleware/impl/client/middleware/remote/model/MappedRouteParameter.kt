package io.lb.middleware.impl.client.middleware.remote.model

import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Data class representing a mapped route.
 *
 * @property path The path of the mapped route.
 * @property originalRoute The original route.
 * @property method The HTTP method of the mapped route.
 * @property preConfiguredQueries The pre-configured queries of the mapped route.
 * @property preConfiguredHeaders The pre-configured headers of the mapped route.
 * @property preConfiguredBody The pre-configured body of the mapped route.
 * @property mappingRules The mapping rules of the mapped route.
 */
@Serializable
internal data class MappedRouteParameter(
    @SerialName("path")
    val path: String,
    @SerialName("originalRoute")
    var originalRoute: OriginalRouteParameter,
    @SerialName("method")
    val method: MiddlewareHttpMethods,
    @SerialName("preConfiguredQueries")
    val preConfiguredQueries: Map<String, String> = mapOf(),
    @SerialName("preConfiguredHeaders")
    val preConfiguredHeaders: Map<String, String> = originalRoute.headers,
    @SerialName("preConfiguredBody")
    val preConfiguredBody: JsonObject? = originalRoute.body,
    @SerialName("rulesAsString")
    val mappingRules: NewBodyMappingRuleParameter?
)
