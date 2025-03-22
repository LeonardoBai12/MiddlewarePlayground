package io.lb.middleware.api.data.data_source

import io.lb.middleware.common.data.middleware.local.MiddlewareDatabaseService
import io.lb.middleware.common.data.user.local.UserDatabaseService
import io.lb.middleware.common.remote.middleware.remote.MiddlewareClientService
import io.lb.middleware.common.remote.middleware.remote.model.MappedRouteResult
import io.lb.middleware.common.shared.middleware.model.MappedApi
import io.lb.middleware.common.shared.middleware.model.MappedRoute
import io.lb.middleware.common.shared.middleware.model.MappingRequest
import io.lb.middleware.common.shared.middleware.model.PreviewRequest
import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods
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

    /**
     * Requests the original route.
     *
     * @param originalBaseUrl The original base URL.
     * @param originalPath The original path.
     * @param originalMethod The original method.
     * @param originalQueries The original queries.
     * @param originalHeaders The original headers.
     * @param originalBody The original body.
     * @return The original route.
     */
    suspend fun testOriginalRoute(
        originalBaseUrl: String,
        originalPath: String,
        originalMethod: MiddlewareHttpMethods,
        originalQueries: Map<String, String> = mapOf(),
        originalHeaders: Map<String, String> = mapOf(),
        originalBody: String?,
    ): String? {
        return middlewareClientService.testOriginalRoute(
            originalBaseUrl = originalBaseUrl,
            originalPath = originalPath,
            originalMethod = originalMethod,
            originalQueries = originalQueries,
            originalHeaders = originalHeaders,
            originalBody = originalBody
        )
    }

    /**
     * Requests the mapped route.
     *
     * @param token The user's token.
     * @param originalBody The original body.
     * @param path The path.
     * @param method The method.
     * @param queries The queries.
     * @param preConfiguredQueries The pre-configured queries.
     * @param preConfiguredHeaders The pre-configured headers.
     * @param preConfiguredBody The pre-configured body.
     * @return The mapped route.
     */
    suspend fun requestMappedRoute(
        token: String,
        path: String,
        method: MiddlewareHttpMethods,
        queries: Map<String, String>,
        preConfiguredQueries: Map<String, String> = mapOf(),
        preConfiguredHeaders: Map<String, String> = mapOf(),
        preConfiguredBody: String?,
    ): String? {
        return middlewareClientService.requestMappedRoute(
            token = token,
            path = path,
            method = method,
            queries = queries,
            preConfiguredQueries = preConfiguredQueries,
            preConfiguredHeaders = preConfiguredHeaders,
            preConfiguredBody = preConfiguredBody
        )
    }
}
