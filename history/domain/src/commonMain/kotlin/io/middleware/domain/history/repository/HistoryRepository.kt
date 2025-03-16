package io.middleware.domain.history.repository

import io.lb.middleware.common.shared.middleware.model.MappedApi
import io.lb.middleware.common.shared.middleware.model.MappedRoute
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource

/**
 * The middleware repository.
 */
interface HistoryRepository {
    /**
     * Get the history of routes.
     */
    suspend fun getRoutesHistory(): CommonFlow<Resource<List<MappedRoute>>>

    /**
     * Get the history of APIs.
     */
    suspend fun getApiHistory(): CommonFlow<Resource<List<MappedApi>>>

    /**
     * Get the route by its API ID.
     *
     * @param apiId The ID of the route.
     * @return The route.
     */
    suspend fun getRoutesByApiIdFromHistory(apiId: String): CommonFlow<Resource<List<MappedRoute>>>

    /**
     * Get the API by its ID.
     *
     * @param routeId The ID of the API.
     * @return The API.
     */
    suspend fun getRouteByIdFromHistory(routeId: String): CommonFlow<Resource<MappedRoute?>>

    /**
     * Mark a route as favourite.
     *
     * @param routeId The ID of the route.
     * @param isFavourite Whether the route is favourite.
     */
    suspend fun switchRouteToFavourite(
        routeId: String,
        isFavourite: Boolean
    ): CommonFlow<Resource<Unit>>

    /**
     * Mark an API as favourite.
     *
     * @param apiId The ID of the API.
     * @param isFavourite Whether the API is favourite.
     */
    suspend fun switchApiToFavourite(
        apiId: String,
        isFavourite: Boolean
    ): CommonFlow<Resource<Unit>>

    /**
     * Delete a route.
     *
     * @param routeId The ID of the route to delete.
     */
    suspend fun deleteRouteFromHistory(routeId: String): CommonFlow<Resource<Unit>>

    /**
     * Delete an API.
     *
     * @param apiId The ID of the API to delete.
     */
    suspend fun deleteApiFromHistory(apiId: String): CommonFlow<Resource<Unit>>

    /**
     * Delete all.
     */
    suspend fun wipeData(): CommonFlow<Resource<Unit>>
}
