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

    /**
     * Mark a route as favourite.
     *
     * @param routeId The ID of the route.
     * @param favourite Whether the route is favourite.
     */
    suspend fun switchRouteToFavourite(routeId: String, favourite: Boolean) {
        middlewareDatabaseService.switchRouteToFavourite(routeId, favourite)
    }

    /**
     * Mark an API as favourite.
     *
     * @param apiId The ID of the API.
     * @param favourite Whether the API is favourite.
     */
    suspend fun switchApiToFavourite(apiId: String, favourite: Boolean) {
        middlewareDatabaseService.switchApiToFavourite(apiId, favourite)
    }
}
