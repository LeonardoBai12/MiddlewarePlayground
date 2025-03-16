package io.middleware.splash.domain.repository

import io.lb.middleware.common.shared.user.UserData

/**
 * The middleware repository.
 */
interface SignUpRepository {
    /**
     * Gets the user's data.
     *
     * @return The user response.
     */
    suspend fun getCurrentUser(): UserData?
}
