package io.lb.middleware.common.shared.middleware.model

import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods

data class MappedRoute(
    val uuid: String,
    val path: String,
    val method: MiddlewareHttpMethods,
    val originalBaseUrl: String,
    val originalPath: String,
    val originalMethod: MiddlewareHttpMethods,
    val originalQueries: Map<String, String> = mapOf(),
    val originalHeaders: Map<String, String> = mapOf(),
    val originalBody: String?,
    val preConfiguredQueries: Map<String, String> = mapOf(),
    val preConfiguredHeaders: Map<String, String> = mapOf(),
    val preConfiguredBody: String? = null,
)
