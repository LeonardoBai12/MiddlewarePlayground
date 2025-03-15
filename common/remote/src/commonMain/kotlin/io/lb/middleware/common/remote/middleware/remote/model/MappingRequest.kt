package io.lb.middleware.common.remote.middleware.remote.model

import io.lb.middleware.common.shared.middleware.model.MappingRules
import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods

data class MappingRequest(
    val path: String,
    val method: MiddlewareHttpMethods,
    val originalBaseUrl: String,
    val originalPath: String,
    val originalMethod: MiddlewareHttpMethods,
    val originalQueries: Map<String, String> = mapOf(),
    val originalHeaders: Map<String, String> = mapOf(),
    val originalBody: String,
    val mappingRules: MappingRules
)
