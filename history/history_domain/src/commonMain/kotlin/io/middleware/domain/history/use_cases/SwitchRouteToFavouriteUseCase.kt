package io.middleware.domain.history.use_cases

import io.middleware.domain.history.repository.HistoryRepository

class SwitchRouteToFavouriteUseCase(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(routeId: String, isFavourite: Boolean) {
        repository.switchRouteToFavourite(routeId, isFavourite)
    }
}