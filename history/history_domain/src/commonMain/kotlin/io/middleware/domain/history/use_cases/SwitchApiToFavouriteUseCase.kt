package io.middleware.domain.history.use_cases

import io.middleware.domain.history.repository.HistoryRepository

class SwitchApiToFavouriteUseCase(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(apiId: String, isFavourite: Boolean) {
        repository.switchApiToFavourite(apiId, isFavourite)
    }
}
