package io.lb.middleware.common.data.user.remote

import io.lb.middleware.common.data.user.remote.model.LoginRequest
import io.lb.middleware.common.data.user.remote.model.UpdatePasswordRequest
import io.lb.middleware.common.data.user.remote.model.UserResult

/**
 * Service for making requests to the middleware server.
 */
interface UserClientService {
    /**
     * Logs in the user.
     *
     * @param data The login request data.
     * @return The user response.
     */
    suspend fun login(data: LoginRequest): UserResult?

    /**
     * Registers the user.
     *
     * @param data The login request data.
     * @return The user response.
     */
    suspend fun register(data: LoginRequest): UserResult?

    /**
     * Gets the user's data.
     *
     * @param token The user's token.
     * @return The user response.
     */
    suspend fun updateUser(token: String, data: LoginRequest): UserResult?

    /**
     * Updates the user's password.
     *
     * @param token The user's token.
     * @param data The update password request data.
     * @return True if the password was successfully updated, false otherwise.
     */
    suspend fun updatePassword(token: String, data: UpdatePasswordRequest): Boolean

    /**
     * Deletes the user.
     *
     * @param token The user's token.
     * @param userId The ID of the user to delete.
     * @return True if the user was successfully deleted, false otherwise.
     */
    suspend fun deleteUser(token: String, userId: String): Boolean

    /**
     * Logs out the user.
     *
     * @param token The user's token.
     * @return True if the user was successfully logged out, false otherwise.
     */
    suspend fun logout(token: String): Boolean
}
