package io.lb.middleware.impl.client.middleware.remote.model

import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Data class representing an original route.
 *
 * @property path The path of the original route.
 * @property originalApi The original API.
 * @property method The HTTP method of the original route.
 * @property queries The queries of the original route.
 * @property headers The headers of the original route.
 * @property body The body of the original route.
 */
@Serializable
internal data class OriginalRouteParameter(
    @SerialName("path")
    val path: String,
    @SerialName("originalApi")
    val originalApi: OriginalApiParameter,
    @SerialName("method")
    val method: MiddlewareHttpMethods,
    @SerialName("queries")
    val queries: Map<String, String> = mapOf(),
    @SerialName("headers")
    val headers: Map<String, String> = mapOf(),
    @SerialName("body")
    val body: JsonObject? = null
)
