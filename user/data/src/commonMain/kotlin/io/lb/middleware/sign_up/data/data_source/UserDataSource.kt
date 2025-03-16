package io.lb.middleware.sign_up.data.data_source

import io.lb.middleware.common.data.user.local.UserDatabaseService
import io.lb.middleware.common.remote.user.remote.UserClientService
import io.lb.middleware.common.remote.user.remote.model.UpdatePasswordRequest
import io.lb.middleware.common.remote.user.remote.model.UserResult
import io.lb.middleware.common.remote.user.remote.model.UserUpdateRequest
import io.lb.middleware.common.shared.user.UserData

class UserDataSource(
    private val userDatabaseService: UserDatabaseService,
    private val userClientService: UserClientService
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
     * Update the user data.
     *
     * @param user The user data.
     */
    suspend fun updateUserLocally(user: UserData) {
        userDatabaseService.updateUser(user)
    }

    /**
     * Delete the user data.
     *
     * @param userId The ID of the user.
     */
    suspend fun deleteUserLocally(userId: String) {
        userDatabaseService.deleteUser(userId)
    }

    /**
     * Gets the user's data.
     *
     * @param token The user's token.
     * @param userId The ID of the user to get.
     * @return The user response.
     */
    suspend fun getUserById(token: String, userId: String): UserResult? {
        return userClientService.getUserById(
            token = token,
            userId = userId
        )
    }

    /**
     * Gets the user's data.
     *
     * @param token The user's token.
     * @return The user response.
     */
    suspend fun updateRemoteUser(token: String, data: UserUpdateRequest): UserResult? {
        return userClientService.updateUser(
            token = token,
            data = data
        )
    }

    /**
     * Updates the user's password.
     *
     * @param token The user's token.
     * @param data The update password request data.
     * @return True if the password was successfully updated, false otherwise.
     */
    suspend fun updateRemotePassword(token: String, data: UpdatePasswordRequest): Boolean {
        return userClientService.updatePassword(
            token = token,
            data = data
        )
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
        return userClientService.deleteUser(
            token = token,
            userId = userId,
            password = password
        )
    }

    /**
     * Logs out the user.
     *
     * @param token The user's token.
     * @return True if the user was successfully logged out, false otherwise.
     */
    suspend fun logout(token: String): Boolean {
        return userClientService.logout(token)
    }
}
