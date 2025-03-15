package io.lb.middleware.common.remote.middleware.remote

import io.lb.middleware.common.remote.middleware.remote.model.MappingRequest
import io.lb.middleware.common.data.middleware.remote.model.PreviewRequest

/**
 * Service for making requests to the middleware server.
 */
interface MiddlewareClientService {
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
    suspend fun getAllRoutes(token: String): List<String>
}
