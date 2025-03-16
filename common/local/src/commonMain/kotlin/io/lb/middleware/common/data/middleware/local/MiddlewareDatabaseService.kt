package io.lb.middleware.common.data.middleware.local

import io.lb.middleware.common.shared.middleware.model.MappedApi
import io.lb.middleware.common.shared.middleware.model.MappedRoute

/**
 * Service interface for interacting with the database.
 */
interface MiddlewareDatabaseService {
    /**
     * Get the history of routes.
     */
    suspend fun getRoutesHistory(): List<MappedRoute>

    /**
     * Get the history of APIs.
     */
    suspend fun getApiHistory(): List<MappedApi>

    /**
     * Get the route by its API ID.
     *
     * @param apiId The ID of the API.
     * @return The route.
     */
    suspend fun getRoutesByApiId(apiId: String): List<MappedRoute>

    /**
     * Get the API by its ID.
     *
     * @param routeId The ID of the API.
     * @return The API.
     */
    suspend fun getRouteById(routeId: String): MappedRoute?

    /**
     * Get the API by its ID.
     *
     * @param apiId The ID of the API.
     * @return The API.
     */
    suspend fun getApiById(apiId: String): MappedApi?

    /**
     * Save a route.
     *
     * @param route The route to save.
     */
    suspend fun saveRoute(route: MappedRoute)

    /**
     * Save an API.
     *
     * @param api The API to save.
     */
    suspend fun saveApi(api: MappedApi)

    /**
     * Delete a route.
     *
     * @param routeId The ID of the route to delete.
     */
    suspend fun deleteRoute(routeId: String)

    /**
     * Delete an API.
     *
     * @param apiId The ID of the API to delete.
     */
    suspend fun deleteApi(apiId: String)

    /**
     * Delete all routes.
     */
    suspend fun deleteAllRoutes()

    /**
     * Delete all APIs.
     */
    suspend fun deleteAllApis()
}
