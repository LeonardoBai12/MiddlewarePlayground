package io.middleware.domain.history.use_cases

import io.middleware.domain.history.repository.HistoryRepository

class DeleteRouteFromHistoryUseCase(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(routeId: String) {
        repository.deleteRouteFromHistory(routeId)
    }
}
