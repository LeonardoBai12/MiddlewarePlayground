package io.middleware.user.domain.repository

import io.lb.middleware.common.shared.user.UserData
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource

/**
 * The middleware repository.
 */
interface UserRepository {
    /**
     * Gets the user's data.
     *
     * @return The user response.
     */
    suspend fun getCurrentUser(): UserData?

    /**
     * Gets the user's data.
     *
     * @param data The user data.
     * @return The user response.
     */
    suspend fun updateUser(data: UserData, password: String): CommonFlow<Resource<UserData?>>

    /**
     * Updates the user's password.
     *
     * @return True if the password was successfully updated, false otherwise.
     */
    suspend fun updatePassword(password: String, newPassword: String): CommonFlow<Resource<Unit>>

    /**
     * Deletes the user.
     *
     * @param password The user's password.
     * @return True if the user was successfully deleted, false otherwise.
     */
    suspend fun deleteUser(password: String): CommonFlow<Resource<Unit>>

    /**
     * Logs out the user.
     *
     * @return True if the user was successfully logged out, false otherwise.
     */
    suspend fun logout(): CommonFlow<Resource<Unit>>
}
