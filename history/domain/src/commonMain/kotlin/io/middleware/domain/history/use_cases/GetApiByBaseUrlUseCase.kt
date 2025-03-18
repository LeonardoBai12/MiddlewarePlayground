package io.middleware.domain.history.use_cases

import io.lb.middleware.common.shared.middleware.model.MappedApi
import io.middleware.domain.history.repository.HistoryRepository

class GetApiByBaseUrlUseCase(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(baseUrl: String): MappedApi? {
        return repository.getApiByBaseUrlFromHistory(baseUrl)
    }
}
