package io.lb.middleware.history.data.data_source

import io.lb.middleware.common.data.middleware.local.MiddlewareDatabaseService
import io.lb.middleware.common.shared.middleware.model.MappedApi
import io.lb.middleware.common.shared.middleware.model.MappedRoute

class HistoryDataSource(
    private val middlewareDatabaseService: MiddlewareDatabaseService,
) {
    /**
     * Get the history of routes.
     */
    suspend fun getRoutesHistory(): List<MappedRoute> {
        return middlewareDatabaseService.getRoutesHistory()
    }

    /**
     * Get the history of APIs.
     */
    suspend fun getApiHistory(): List<MappedApi> {
        return middlewareDatabaseService.getApiHistory()
    }

    /**
     * Get the route by its API ID.
     *
     * @param apiId The ID of the API.
     * @return The route.
     */
    suspend fun getRoutesByApiIdLocally(apiId: String): List<MappedRoute> {
        return middlewareDatabaseService.getRoutesByApiId(apiId)
    }

    /**
     * Get the API by its ID.
     *
     * @param routeId The ID of the API.
     * @return The API.
     */
    suspend fun getRouteByIdLocally(routeId: String): MappedRoute? {
        return middlewareDatabaseService.getRouteById(routeId)
    }

    /**
     * Get the API by its ID.
     *
     * @param apiId The ID of the API.
     * @return The API.
     */
    suspend fun getApiById(apiId: String): MappedApi? {
        return middlewareDatabaseService.getApiById(apiId)
    }

    /**
     * Delete a route.
     *
     * @param routeId The ID of the route to delete.
     */
    suspend fun deleteRoute(routeId: String) {
        middlewareDatabaseService.deleteRoute(routeId)
    }

    /**
     * Delete an API.
     *
     * @param apiId The ID of the API to delete.
     */
    suspend fun deleteApi(apiId: String) {
        middlewareDatabaseService.deleteApi(apiId)
    }

    /**
     * Delete all routes.
     */
    suspend fun deleteAllRoutes() {
        middlewareDatabaseService.deleteAllRoutes()
    }

    /**
     * Delete all APIs.
     */
    suspend fun deleteAllApis() {
        middlewareDatabaseService.deleteAllApis()
    }
}
