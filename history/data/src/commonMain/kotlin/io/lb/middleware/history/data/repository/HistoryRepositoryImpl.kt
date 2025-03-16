package io.lb.middleware.history.data.repository

import io.lb.middleware.common.shared.middleware.model.MappedApi
import io.lb.middleware.common.shared.middleware.model.MappedRoute
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource
import io.lb.middleware.history.data.data_source.HistoryDataSource
import io.middleware.domain.history.repository.HistoryRepository

class HistoryRepositoryImpl(
    private val dataSource: HistoryDataSource
) : HistoryRepository {
    override suspend fun getRoutesHistory(): CommonFlow<Resource<List<MappedRoute>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getApiHistory(): CommonFlow<Resource<List<MappedApi>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRoutesByApiIdFromHistory(apiId: String): CommonFlow<Resource<List<MappedRoute>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRouteByIdFromHistory(routeId: String): MappedRoute? {
        TODO("Not yet implemented")
    }

    override suspend fun switchRouteToFavourite(routeId: String, isFavourite: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun switchApiToFavourite(apiId: String, isFavourite: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRouteFromHistory(routeId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteApiFromHistory(apiId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun wipeData() {
        TODO("Not yet implemented")
    }

}
