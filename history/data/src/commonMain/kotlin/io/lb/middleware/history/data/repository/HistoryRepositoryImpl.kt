package io.lb.middleware.history.data.repository

import io.lb.middleware.common.shared.middleware.model.MappedApi
import io.lb.middleware.common.shared.middleware.model.MappedRoute
import io.lb.middleware.history.data.data_source.HistoryDataSource
import io.middleware.domain.history.repository.HistoryRepository

class HistoryRepositoryImpl(
    private val dataSource: HistoryDataSource
) : HistoryRepository {
    override suspend fun getRoutesHistory(): List<MappedRoute> {
        return dataSource.getRoutesHistory()
    }

    override suspend fun getApiHistory(): List<MappedApi> {
        return dataSource.getApiHistory()
    }

    override suspend fun getRoutesByApiIdFromHistory(apiId: String): List<MappedRoute> {
        return dataSource.getRoutesByApiIdLocally(apiId)
    }

    override suspend fun getRouteByIdFromHistory(routeId: String): MappedRoute? {
        return dataSource.getRouteByIdLocally(routeId)
    }

    override suspend fun getApiByIdFromHistory(apiId: String): MappedApi? {
        return dataSource.getApiById(apiId)
    }

    override suspend fun switchRouteToFavourite(routeId: String, isFavourite: Boolean) {
        dataSource.switchRouteToFavourite(routeId, isFavourite)
    }

    override suspend fun switchApiToFavourite(apiId: String, isFavourite: Boolean) {
        dataSource.switchApiToFavourite(apiId, isFavourite)
    }

    override suspend fun deleteRouteFromHistory(routeId: String) {
        dataSource.deleteRoute(routeId)
    }

    override suspend fun deleteApiFromHistory(apiId: String) {
        dataSource.deleteApi(apiId)
    }

    override suspend fun wipeData() {
        dataSource.deleteAllApis()
        dataSource.deleteAllRoutes()
    }
}
