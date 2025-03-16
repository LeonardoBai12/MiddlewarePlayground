package io.lb.middleware.sign_up.data.data_source

import io.lb.middleware.common.data.user.local.UserDatabaseService
import io.lb.middleware.common.remote.user.remote.UserClientService
import io.lb.middleware.common.remote.user.remote.model.LoginRequest
import io.lb.middleware.common.remote.user.remote.model.LoginResult
import io.lb.middleware.common.remote.user.remote.model.UserCreateRequest
import io.lb.middleware.common.remote.user.remote.model.UserResult
import io.lb.middleware.common.shared.user.UserData

class MiddlewareDataSource(
    private val userDatabaseService: UserDatabaseService,
    private val userClientService: UserClientService
) {
    /**
     * Get the user data.
     *
     * @param user The ID of the user.
     */
    suspend fun saveUserLocally(user: UserData) {
        userDatabaseService.saveUser(user)
    }

    /**
     * Logs in the user.
     *
     * @param data The login request data.
     * @return The user response.
     */
    suspend fun login(data: LoginRequest): LoginResult? {
        return userClientService.login(data)
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
     * Registers the user.
     *
     * @param data The login request data.
     * @return The user response.
     */
    suspend fun signUp(data: UserCreateRequest): UserResult? {
        return userClientService.signUp(data)
    }
}
