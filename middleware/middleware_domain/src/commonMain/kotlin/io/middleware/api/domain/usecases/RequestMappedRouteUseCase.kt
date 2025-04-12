package io.middleware.api.domain.usecases

import io.lb.middleware.common.shared.middleware.error.MiddlewareException
import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods
import io.middleware.api.domain.repository.MiddlewareRepository

class RequestMappedRouteUseCase(
    private val repository: MiddlewareRepository
) {
    suspend operator fun invoke(
        path: String,
        method: MiddlewareHttpMethods,
        queries: Map<String, String>,
        preConfiguredQueries: Map<String, String>,
        preConfiguredHeaders: Map<String, String>,
        preConfiguredBody: String?,
    ): String? {
        if (path.isBlank()) {
            throw MiddlewareException("Original path is empty")
        }
        return repository.requestMappedRoute(
            path = path,
            method = method,
            queries = queries,
            preConfiguredQueries = preConfiguredQueries,
            preConfiguredHeaders = preConfiguredHeaders,
            preConfiguredBody = preConfiguredBody
        )
    }
}
