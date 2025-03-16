package io.middleware.domain.history.repository

import io.lb.middleware.common.shared.middleware.model.MappedApi
import io.lb.middleware.common.shared.middleware.model.MappedRoute

/**
 * The middleware repository.
 */
interface HistoryRepository {
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
     * @param apiId The ID of the route.
     * @return The route.
     */
    suspend fun getRoutesByApiIdFromHistory(apiId: String): List<MappedRoute>

    /**
     * Get the API by its ID.
     *
     * @param routeId The ID of the API.
     * @return The API.
     */
    suspend fun getRouteByIdFromHistory(routeId: String): MappedRoute?

    /**
     * Get the API by its ID.
     *
     * @param apiId The ID of the API.
     * @return The API.
     */
    suspend fun getApiByIdFromHistory(apiId: String): MappedApi?

    /**
     * Mark a route as favourite.
     *
     * @param routeId The ID of the route.
     * @param isFavourite Whether the route is favourite.
     */
    suspend fun switchRouteToFavourite(
        routeId: String,
        isFavourite: Boolean
    )

    /**
     * Mark an API as favourite.
     *
     * @param apiId The ID of the API.
     * @param isFavourite Whether the API is favourite.
     */
    suspend fun switchApiToFavourite(
        apiId: String,
        isFavourite: Boolean
    )

    /**
     * Delete a route.
     *
     * @param routeId The ID of the route to delete.
     */
    suspend fun deleteRouteFromHistory(routeId: String)

    /**
     * Delete an API.
     *
     * @param apiId The ID of the API to delete.
     */
    suspend fun deleteApiFromHistory(apiId: String)

    /**
     * Delete all.
     */
    suspend fun wipeData()
}
