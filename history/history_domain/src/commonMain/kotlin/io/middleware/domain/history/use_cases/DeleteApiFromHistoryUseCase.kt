package io.middleware.domain.history.use_cases

import io.middleware.domain.history.repository.HistoryRepository

class DeleteApiFromHistoryUseCase(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(apiId: String) {
        repository.deleteApiFromHistory(apiId)
    }
}
