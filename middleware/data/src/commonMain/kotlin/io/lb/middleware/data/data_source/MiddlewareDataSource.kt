package io.lb.middleware.data.data_source

import io.lb.middleware.common.data.middleware.local.MiddlewareDatabaseService
import io.lb.middleware.common.data.middleware.remote.model.PreviewRequest
import io.lb.middleware.common.data.user.local.UserDatabaseService
import io.lb.middleware.common.remote.middleware.remote.MiddlewareClientService
import io.lb.middleware.common.remote.middleware.remote.model.MappedRouteResult
import io.lb.middleware.common.remote.middleware.remote.model.MappingRequest
import io.lb.middleware.common.remote.user.remote.UserClientService
import io.lb.middleware.common.remote.user.remote.model.LoginRequest
import io.lb.middleware.common.remote.user.remote.model.LoginResult
import io.lb.middleware.common.remote.user.remote.model.UpdatePasswordRequest
import io.lb.middleware.common.remote.user.remote.model.UserCreateRequest
import io.lb.middleware.common.remote.user.remote.model.UserResult
import io.lb.middleware.common.remote.user.remote.model.UserUpdateRequest
import io.lb.middleware.common.shared.middleware.model.MappedApi
import io.lb.middleware.common.shared.middleware.model.MappedRoute
import io.lb.middleware.common.shared.user.UserData

class MiddlewareDataSource(
    private val middlewareDatabaseService: MiddlewareDatabaseService,
    private val middlewareClientService: MiddlewareClientService,
    private val userDatabaseService: UserDatabaseService,
    private val userClientService: UserClientService
) {
    /**
     * Get the user data.
     *
     * @param user The ID of the user.
     */
    suspend fun saveUserLocally(user: UserData) {
        TODO()
    }

    /**
     * Get the user data.
     *
     * @return The user data.
     */
    suspend fun getCurrentUser(): UserData? {
        TODO()
    }

    /**
     * Update the user data.
     *
     * @param user The user data.
     */
    suspend fun updateUserLocally(user: UserData) {
        TODO()
    }

    /**
     * Delete the user data.
     *
     * @param userId The ID of the user.
     */
    suspend fun deleteUserLocally(userId: String) {
        TODO()
    }

    /**
     * Logs in the user.
     *
     * @param data The login request data.
     * @return The user response.
     */
    suspend fun login(data: LoginRequest): LoginResult? {
        TODO()
    }

    /**
     * Gets the user's data.
     *
     * @param token The user's token.
     * @param userId The ID of the user to get.
     * @return The user response.
     */
    suspend fun getUserById(token: String, userId: String): UserResult? {
        TODO()
    }

    /**
     * Registers the user.
     *
     * @param data The login request data.
     * @return The user response.
     */
    suspend fun signUp(data: UserCreateRequest): UserResult? {
        TODO()
    }

    /**
     * Gets the user's data.
     *
     * @param token The user's token.
     * @return The user response.
     */
    suspend fun updateRemoteUser(token: String, data: UserUpdateRequest): UserResult? {
        TODO()
    }

    /**
     * Updates the user's password.
     *
     * @param token The user's token.
     * @param data The update password request data.
     * @return True if the password was successfully updated, false otherwise.
     */
    suspend fun updateRemotePassword(token: String, data: UpdatePasswordRequest): Boolean {
        TODO()
    }

    /**
     * Deletes the user.
     *
     * @param token The user's token.
     * @param userId The ID of the user to delete.
     * @param password The user's password.
     * @return True if the user was successfully deleted, false otherwise.
     */
    suspend fun deleteRemoteUser(token: String, userId: String, password: String): Boolean {
        TODO()
    }

    /**
     * Logs out the user.
     *
     * @param token The user's token.
     * @return True if the user was successfully logged out, false otherwise.
     */
    suspend fun logout(token: String): Boolean {
        TODO()
    }

    /**
     * Get the history of routes.
     */
    suspend fun getRoutesHistory(): List<MappedRoute> {
        TODO()
    }

    /**
     * Get the history of APIs.
     */
    suspend fun getApiHistory(): List<MappedApi> {
        TODO()
    }

    /**
     * Get the route by its API ID.
     *
     * @param apiId The ID of the API.
     * @return The route.
     */
    suspend fun getRoutesByApiIdLocally(apiId: String): List<MappedRoute> {
        TODO()
    }

    /**
     * Get the API by its ID.
     *
     * @param routeId The ID of the API.
     * @return The API.
     */
    suspend fun getRouteByIdLocally(routeId: String): MappedRoute? {
        TODO()
    }

    /**
     * Get the API by its ID.
     *
     * @param apiId The ID of the API.
     * @return The API.
     */
    suspend fun getApiById(apiId: String): MappedApi? {
        TODO()
    }

    /**
     * Save a route.
     *
     * @param route The route to save.
     */
    suspend fun saveRoute(route: MappedRoute) {
        TODO()
    }

    /**
     * Save an API.
     *
     * @param api The API to save.
     */
    suspend fun saveApi(api: MappedRoute) {
        TODO()
    }

    /**
     * Delete a route.
     *
     * @param routeId The ID of the route to delete.
     */
    suspend fun deleteRoute(routeId: String) {
        TODO()
    }

    /**
     * Delete an API.
     *
     * @param apiId The ID of the API to delete.
     */
    suspend fun deleteApi(apiId: String) {
        TODO()
    }

    /**
     * Delete all routes.
     */
    suspend fun deleteAllRoutes() {
        TODO()
    }

    /**
     * Delete all APIs.
     */
    suspend fun deleteAllApis() {
        TODO()
    }

    /**
     * Requests a preview of the mapping.
     *
     * @param token The user's token.
     * @param data The preview request data.
     * @return The preview response.
     */
    suspend fun requestPreview(token: String, data: PreviewRequest): String {
        TODO()
    }

    /**
     * Creates a new route.
     *
     * @param token The user's token.
     * @param data The mapping request data.
     * @return The new route ID.
     */
    suspend fun createNewRoute(token: String, data: MappingRequest): String {
        TODO()
    }

    /**
     * Gets all routes.
     *
     * @param token The user's token.
     * @return The list of routes.
     */
    suspend fun getAllRoutes(token: String): List<MappedRouteResult> {
        TODO()
    }
}
