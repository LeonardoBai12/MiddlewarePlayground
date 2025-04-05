package io.middleware.domain.history.use_cases

import io.lb.middleware.common.shared.middleware.model.MappedRoute
import io.middleware.domain.history.repository.HistoryRepository

class GetRoutesByApiIdUseCase(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(apiId: String): List<MappedRoute> {
        return repository.getRoutesByApiIdFromHistory(apiId)
    }
}
