package io.lb.middleware.api.data.data_source

import io.lb.middleware.common.data.middleware.local.MiddlewareDatabaseService
import io.lb.middleware.common.data.user.local.UserDatabaseService
import io.lb.middleware.common.remote.middleware.remote.MiddlewareClientService
import io.lb.middleware.common.remote.middleware.remote.model.MappedRouteResult
import io.lb.middleware.common.shared.middleware.model.MappedApi
import io.lb.middleware.common.shared.middleware.model.MappedRoute
import io.lb.middleware.common.shared.middleware.model.MappingRequest
import io.lb.middleware.common.shared.middleware.model.PreviewRequest
import io.lb.middleware.common.shared.user.UserData

class MiddlewareDataSource(
    private val middlewareDatabaseService: MiddlewareDatabaseService,
    private val middlewareClientService: MiddlewareClientService,
    private val userDatabaseService: UserDatabaseService
) {
    /**
     * Get the user data.
     *
     * @return The user data.
     */
    suspend fun getCurrentUser(): UserData? {
        return userDatabaseService.getCurrentUser()
    }

    /**
     * Save a route.
     *
     * @param route The route to save.
     */
    suspend fun saveRoute(route: MappedRoute) {
        middlewareDatabaseService.saveRoute(route)
    }

    /**
     * Save an API.
     *
     * @param api The API to save.
     */
    suspend fun saveApi(api: MappedApi) {
        middlewareDatabaseService.saveApi(api)
    }

    /**
     * Gets API by ID.
     *
     * @param apiBaseUrl The URL of the API.
     */
    suspend fun getApiByBaseUrl(apiBaseUrl: String): MappedApi? {
        return middlewareDatabaseService.getApiByBaseUrl(apiBaseUrl)
    }

    /**
     * Requests a preview of the mapping.
     *
     * @param token The user's token.
     * @param data The preview request data.
     * @return The preview response.
     */
    suspend fun requestPreview(token: String, data: PreviewRequest): String {
        return middlewareClientService.requestPreview(
            token = token,
            data = data
        )
    }

    /**
     * Creates a new route.
     *
     * @param token The user's token.
     * @param data The mapping request data.
     * @return The new route ID.
     */
    suspend fun createNewRoute(token: String, data: MappingRequest): String {
        return middlewareClientService.createNewRoute(
            token = token,
            data = data
        )
    }

    /**
     * Gets all routes.
     *
     * @param token The user's token.
     * @return The list of routes.
     */
    suspend fun getAllRoutes(token: String): List<MappedRouteResult> {
        return middlewareClientService.getAllRoutes(token)
    }
}
