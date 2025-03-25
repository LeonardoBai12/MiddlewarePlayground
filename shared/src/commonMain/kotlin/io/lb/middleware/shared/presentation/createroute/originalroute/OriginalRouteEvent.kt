package io.lb.middleware.shared.presentation.createroute.originalroute

import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods

sealed class OriginalRouteEvent {
    data class UpsertOriginalQuery(val key: String, val value: String) : OriginalRouteEvent()
    data class RemoveOriginalQuery(val key: String) : OriginalRouteEvent()
    data class UpsertOriginalHeader(val key: String, val value: String) : OriginalRouteEvent()
    data class RemoveOriginalHeader(val key: String) : OriginalRouteEvent()
    data class TestOriginalRoute(
        val originalBaseUrl: String,
        val originalPath: String,
        val originalMethod: MiddlewareHttpMethods,
        val originalBody: String?,
    ) : OriginalRouteEvent()
}
