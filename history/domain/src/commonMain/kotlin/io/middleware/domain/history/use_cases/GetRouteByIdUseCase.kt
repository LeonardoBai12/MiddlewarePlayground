package io.middleware.domain.history.use_cases

import io.lb.middleware.common.shared.middleware.model.MappedRoute
import io.middleware.domain.history.repository.HistoryRepository

class GetRouteByIdUseCase(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(routeId: String): MappedRoute? {
        return repository.getRouteByIdFromHistory(routeId)
    }
}
