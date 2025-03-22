package io.lb.middleware.common.remote.middleware.remote

import io.lb.middleware.common.remote.middleware.remote.model.MappedRouteResult
import io.lb.middleware.common.shared.middleware.model.MappingRequest
import io.lb.middleware.common.shared.middleware.model.PreviewRequest
import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods

/**
 * Service for making requests to the middleware server.
 */
interface MiddlewareClientService {
    /**
     * Requests a mapped route.
     *
     * @param token The user's token.
     * @param originalBody The original body.
     * @param path The path.
     * @param queries The queries to add to the request.
     * @param method The method.
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
    ): String?

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
     * @param token The user's token.
     * @param data The preview request data.
     * @return The preview response.
     */
    suspend fun requestPreview(token: String, data: PreviewRequest): String

    /**
     * Creates a new route.
     *
     * @param token The user's token.
     * @param data The mapping request data.
     * @return The new route ID.
     */
    suspend fun createNewRoute(token: String, data: MappingRequest): String

    /**
     * Gets all routes.
     *
     * @param token The user's token.
     * @return The list of routes.
     */
    suspend fun getAllRoutes(token: String): List<MappedRouteResult>
}
