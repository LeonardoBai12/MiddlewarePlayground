package io.middleware.api.domain.repository

import io.lb.middleware.common.shared.middleware.model.MappedRoute
import io.lb.middleware.common.shared.middleware.model.MappingRequest
import io.lb.middleware.common.shared.middleware.model.PreviewRequest
import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource

/**
 * The middleware repository.
 */
interface MiddlewareRepository {
    /**
     * Requests a mapped route.
     *
     * @param path The path.
     * @param queries The queries to add to the request.
     * @param method The method.
     * @param preConfiguredQueries The pre-configured queries.
     * @param preConfiguredHeaders The pre-configured headers.
     * @param preConfiguredBody The pre-configured body.
     * @return The mapped route.
     */
    suspend fun requestMappedRoute(
        path: String,
        method: MiddlewareHttpMethods,
        queries: Map<String, String>,
        preConfiguredQueries: Map<String, String> = mapOf(),
        preConfiguredHeaders: Map<String, String> = mapOf(),
        preConfiguredBody: String?,
    ): String?

    /**
     * Saves a route in history.
     *
     * @param route The route to save.
     */
    suspend fun saveRouteInHistory(route: MappedRoute)

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
    ): String?

    /**
     * Requests a preview of the mapping.
     *
     * @param data The preview request data.
     * @return The preview response.
     */
    suspend fun requestPreview(data: PreviewRequest): CommonFlow<Resource<String>>

    /**
     * Creates a new route.
     *
     * @param data The mapping request data.
     * @return The new route ID.
     */
    suspend fun createNewRoute(data: MappingRequest): CommonFlow<Resource<String>>

    /**
     * Gets all routes.
     *
     * @return The list of routes.
     */
    suspend fun getAllRoutes(): CommonFlow<Resource<List<MappedRoute>>>
}
