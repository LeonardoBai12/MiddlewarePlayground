package io.middleware.api.domain.use_cases

import io.lb.middleware.common.shared.middleware.error.MiddlewareException
import io.lb.middleware.common.shared.middleware.model.MappedRoute
import io.middleware.api.domain.repository.MiddlewareRepository

class SaveRouteInHistoryUseCase(
    private val repository: MiddlewareRepository
) {
    suspend operator fun invoke(mappedRoute: MappedRoute) {
        if (mappedRoute.path.isBlank()) {
            throw MiddlewareException("Original path is empty")
        }
        return repository.saveRouteInHistory(mappedRoute)
    }
}
