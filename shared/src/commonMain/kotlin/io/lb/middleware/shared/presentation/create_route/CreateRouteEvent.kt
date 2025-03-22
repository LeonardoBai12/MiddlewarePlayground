package io.lb.middleware.shared.presentation.create_route

import io.lb.middleware.common.shared.middleware.model.NewBodyField
import io.lb.middleware.common.shared.middleware.model.OldBodyField
import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods

sealed class CreateRouteEvent {
    data class UpdateOriginalResponse(val response: String) : CreateRouteEvent()
    data class UpsertNewBodyField(val key: String, val field: NewBodyField) : CreateRouteEvent()
    data class UpsertOldBodyField(val key: String, val field: OldBodyField) : CreateRouteEvent()
    data class RemoveNewBodyField(val key: String) : CreateRouteEvent()
    data class RemoveOldBodyField(val key: String) : CreateRouteEvent()
    data class TestOriginalRoute(
        val originalBaseUrl: String,
        val originalPath: String,
        val originalMethod: MiddlewareHttpMethods,
        val originalBody: String?,
    ) : CreateRouteEvent()
    data object RequestPreview : CreateRouteEvent()
}
