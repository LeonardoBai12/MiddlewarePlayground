package io.lb.middleware.shared.presentation.create_route.review

import io.lb.middleware.common.shared.middleware.model.NewBodyField
import io.lb.middleware.common.shared.middleware.model.OldBodyField
import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods

sealed class ReviewEvent {
    data class CreateMappedRoute(
        val originalBaseUrl: String,
        val originalPath: String,
        val originalMethod: MiddlewareHttpMethods,
        val originalBody: String?,
        val originalQueries: Map<String, String> = emptyMap(),
        val originalHeaders: Map<String, String> = emptyMap(),
        val newBodyFields: Map<String, NewBodyField> = emptyMap(),
        val oldBodyFields: Map<String, OldBodyField> = emptyMap(),
        val preConfiguredQueries: Map<String, String> = emptyMap(),
        val preConfiguredHeaders: Map<String, String> = emptyMap(),
        val mappedBaseUrl: String,
        val mappedPath: String,
        val mappedMethod: MiddlewareHttpMethods,
        val mappedBody: String?,
        val preConfiguredBody: String?,
        val ignoreEmptyValues: Boolean
    ) : ReviewEvent()
}
