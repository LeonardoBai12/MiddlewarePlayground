package io.middleware.domain.history.use_cases

import io.lb.middleware.common.shared.middleware.model.MappedRoute
import io.middleware.domain.history.repository.HistoryRepository

class GetRoutesHistoryUseCase(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(): List<MappedRoute> {
        return repository.getRoutesHistory()
    }
}
