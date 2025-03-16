package io.middleware.api.domain.repository

import io.lb.middleware.common.shared.middleware.model.MappedRoute
import io.lb.middleware.common.shared.middleware.model.MappingRequest
import io.lb.middleware.common.shared.middleware.model.PreviewRequest
import io.lb.middleware.common.shared.user.UserData
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource

/**
 * The middleware repository.
 */
interface MiddlewareRepository {
    /**
     * Gets the user's data.
     *
     * @return The user response.
     */
    suspend fun getCurrentUser(): CommonFlow<UserData?>

    /**
     * Requests a preview of the mapping.
     *
     * @param data The preview request data.
     * @return The preview response.
     */
    suspend fun requestPreview(data: PreviewRequest): CommonFlow<String>

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
