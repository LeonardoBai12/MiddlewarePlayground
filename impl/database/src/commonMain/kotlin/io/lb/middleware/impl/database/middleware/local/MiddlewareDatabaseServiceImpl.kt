package io.lb.middleware.impl.database.middleware.local

import io.lb.middleware.common.data.middleware.local.MiddlewareDatabaseService
import io.lb.middleware.common.data.middleware.local.model.MappedApiEntity
import io.lb.middleware.common.data.middleware.local.model.MappedRouteEntity

/**
 * Service interface for interacting with the database.
 */
class MiddlewareDatabaseServiceImpl(
    val dataBase: MiddlewareDatabase
) : MiddlewareDatabaseService {
    override suspend fun getRoutesHistory(): List<MappedRouteEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getApiHistory(): List<MappedApiEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getRouteBysApiId(routeId: String): List<MappedRouteEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getRouteById(routeId: String): MappedRouteEntity? {
        TODO("Not yet implemented")
    }

    override suspend fun getApiById(apiId: String): MappedApiEntity? {
        TODO("Not yet implemented")
    }

    override suspend fun saveRoute(route: MappedRouteEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun saveApi(api: MappedApiEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRoute(routeId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteApi(apiId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllRoutes() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllApis() {
        TODO("Not yet implemented")
    }
}
