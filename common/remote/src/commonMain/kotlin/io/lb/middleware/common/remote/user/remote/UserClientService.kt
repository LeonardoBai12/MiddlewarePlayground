package io.lb.middleware.common.remote.user.remote

import io.lb.middleware.common.remote.user.remote.model.LoginRequest
import io.lb.middleware.common.remote.user.remote.model.UpdatePasswordRequest
import io.lb.middleware.common.remote.user.remote.model.LoginResult
import io.lb.middleware.common.remote.user.remote.model.UserCreateRequest
import io.lb.middleware.common.remote.user.remote.model.UserResult
import io.lb.middleware.common.remote.user.remote.model.UserUpdateRequest

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
    suspend fun login(data: LoginRequest): LoginResult?

    /**
     * Gets the user's data.
     *
     * @param token The user's token.
     * @param userId The ID of the user to get.
     * @return The user response.
     */
    suspend fun getUserById(token: String, userId: String): UserResult?

    /**
     * Registers the user.
     *
     * @param data The login request data.
     * @return The user response.
     */
    suspend fun signUp(data: UserCreateRequest): UserResult?

    /**
     * Gets the user's data.
     *
     * @param token The user's token.
     * @return The user response.
     */
    suspend fun updateUser(token: String, data: UserUpdateRequest): UserResult?

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
     * @param password The user's password.
     * @return True if the user was successfully deleted, false otherwise.
     */
    suspend fun deleteUser(token: String, userId: String, password: String): Boolean

    /**
     * Logs out the user.
     *
     * @param token The user's token.
     * @return True if the user was successfully logged out, false otherwise.
     */
    suspend fun logout(token: String): Boolean
}
