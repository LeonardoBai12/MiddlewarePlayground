package io.middleware.domain.history.use_cases

import io.middleware.domain.history.repository.HistoryRepository

class WipeDataUseCase(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke() {
        repository.wipeData()
    }
}
