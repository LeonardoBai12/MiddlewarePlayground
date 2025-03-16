package io.middleware.domain.repository

import io.lb.middleware.common.shared.middleware.model.MappedApi
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
    suspend fun getRoutesByApiId(apiId: String): CommonFlow<Resource<List<MappedRoute>>>

    /**
     * Get the API by its ID.
     *
     * @param routeId The ID of the API.
     * @return The API.
     */
    suspend fun getRouteById(routeId: String): CommonFlow<Resource<MappedRoute?>>

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
    suspend fun deleteApi(apiId: String): CommonFlow<Resource<Unit>>

    /**
     * Delete all routes.
     */
    suspend fun deleteAllRoutes(): CommonFlow<Resource<Unit>>

    /**
     * Delete all APIs.
     */
    suspend fun deleteAllApis(): CommonFlow<Resource<Unit>>

    /**
     * Logs in the user.
     *
     * @param email The user's email.
     * @param password The user's password.
     * @return The user response.
     */
    suspend fun login(email: String, password: String): CommonFlow<UserData?>

    /**
     * Gets the user's data.
     *
     * @return The user response.
     */
    suspend fun getCurrentUser(): CommonFlow<UserData?>

    /**
     * Registers the user.
     *
     * @param data The login request data.
     * @return The user response.
     */
    suspend fun signUp(data: UserData): CommonFlow<Resource<Unit>>

    /**
     * Gets the user's data.
     *
     * @param data The user data.
     * @return The user response.
     */
    suspend fun updateUser(data: UserData): CommonFlow<UserData?>

    /**
     * Updates the user's password.
     *
     * @return True if the password was successfully updated, false otherwise.
     */
    suspend fun updatePassword(password: String, newPassword: String): CommonFlow<Resource<Unit>>

    /**
     * Deletes the user.
     *
     * @param userId The ID of the user to delete.
     * @param password The user's password.
     * @return True if the user was successfully deleted, false otherwise.
     */
    suspend fun deleteUser(userId: String, password: String): CommonFlow<Resource<Unit>>

    /**
     * Logs out the user.
     *
     * @return True if the user was successfully logged out, false otherwise.
     */
    suspend fun logout(): CommonFlow<Resource<Unit>>

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
