package io.middleware.splash.domain.repository

import io.lb.middleware.common.shared.user.UserData
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource

/**
 * The middleware repository.
 */
interface SignUpRepository {
    /**
     * Gets the user's data.
     *
     * @return The user response.
     */
    suspend fun getCurrentUser(): CommonFlow<UserData?>
}
