package io.lb.middleware.shared.presentation.create_route

import io.lb.middleware.common.shared.middleware.model.NewBodyField
import io.lb.middleware.common.shared.middleware.model.OldBodyField
import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods

sealed class CreateRouteEvent {
    data class UpsertNewBodyField(val key: String, val field: NewBodyField) : CreateRouteEvent()
    data class RemoveNewBodyField(val key: String) : CreateRouteEvent()
    data class UpsertOldBodyField(val key: String, val field: OldBodyField) : CreateRouteEvent()
    data class RemoveOldBodyField(val key: String) : CreateRouteEvent()
    data class UpsertOriginalQuery(val key: String, val value: String) : CreateRouteEvent()
    data class RemoveOriginalQuery(val key: String) : CreateRouteEvent()
    data class UpsertOriginalHeader(val key: String, val value: String) : CreateRouteEvent()
    data class RemoveOriginalHeader(val key: String) : CreateRouteEvent()
    data class UpsertPreConfiguredQuery(val key: String, val value: String) : CreateRouteEvent()
    data class RemovePreConfiguredQuery(val key: String) : CreateRouteEvent()
    data class UpsertPreConfiguredHeader(val key: String, val value: String) : CreateRouteEvent()
    data class RemovePreConfiguredHeader(val key: String) : CreateRouteEvent()
    data class TestOriginalRoute(
        val originalBaseUrl: String,
        val originalPath: String,
        val originalMethod: MiddlewareHttpMethods,
        val originalBody: String?,
    ) : CreateRouteEvent()
    data class RequestPreview(
        val originalBaseUrl: String,
        val originalPath: String,
        val originalMethod: MiddlewareHttpMethods,
        val originalBody: String?,
    ) : CreateRouteEvent()
    data class CreateMappedRoute(
        val originalBaseUrl: String,
        val originalPath: String,
        val originalMethod: MiddlewareHttpMethods,
        val originalBody: String?,
        val mappedBaseUrl: String,
        val mappedPath: String,
        val mappedMethod: MiddlewareHttpMethods,
        val mappedBody: String?,
        val preConfiguredBody: String?,
    ) : CreateRouteEvent()
}
