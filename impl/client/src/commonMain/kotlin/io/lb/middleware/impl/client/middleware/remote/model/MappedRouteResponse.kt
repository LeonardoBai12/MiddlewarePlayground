package io.lb.middleware.impl.client.middleware.remote.model

import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods
import kotlinx.serialization.json.JsonObject

internal data class MappedRouteResponse(
    val uuid: String,
    val path: String,
    val originalRoute: OriginalRouteParameter,
    val method: MiddlewareHttpMethods,
    val preConfiguredQueries: Map<String, String> = mapOf(),
    val preConfiguredHeaders: Map<String, String> = mapOf(),
    val preConfiguredBody: JsonObject? = null,
    val rulesAsString: NewBodyMappingRuleParameter?
)
