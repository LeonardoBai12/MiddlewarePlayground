package io.middleware.api.domain.use_cases

import io.lb.middleware.common.shared.middleware.error.MiddlewareException
import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods
import io.middleware.api.domain.repository.MiddlewareRepository

class TestOriginalRouteUseCase(
    private val repository: MiddlewareRepository
) {
    suspend operator fun invoke(
        originalBaseUrl: String,
        originalPath: String,
        originalMethod: MiddlewareHttpMethods,
        originalQueries: Map<String, String>,
        originalHeaders: Map<String, String>,
        originalBody: String?,
    ): String? {
        if (originalPath.isBlank()) {
            throw MiddlewareException("Original path is empty")
        }
        if (originalBaseUrl.isBlank()) {
            throw MiddlewareException("Original base url is empty")
        }
        return repository.testOriginalRoute(
            originalBaseUrl = originalBaseUrl,
            originalPath = originalPath,
            originalMethod = originalMethod,
            originalQueries = originalQueries,
            originalHeaders = originalHeaders,
            originalBody = originalBody
        )
    }
}
