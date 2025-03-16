package io.middleware.sign_up.domain.repository

import io.lb.middleware.common.shared.user.UserData
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource

/**
 * The middleware repository.
 */
interface SignUpRepository {
    /**
     * Logs in the user.
     *
     * @param email The user's email.
     * @param password The user's password.
     * @return The user response.
     */
    suspend fun login(email: String, password: String): CommonFlow<Resource<UserData?>>

    /**
     * Registers the user.
     *
     * @param data The login request data.
     * @return The user response.
     */
    suspend fun signUp(data: UserData, password: String): CommonFlow<Resource<UserData?>>
}
