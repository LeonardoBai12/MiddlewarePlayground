package io.middleware.domain.history.use_cases

import io.lb.middleware.common.shared.middleware.model.MappedApi
import io.middleware.domain.history.repository.HistoryRepository

class GetApiHistoryUseCase(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(): List<MappedApi> {
        return repository.getApiHistory()
    }
}
