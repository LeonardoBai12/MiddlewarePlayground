package io.lb.middleware.splash.data.datasource

import io.lb.middleware.common.data.user.local.UserDatabaseService
import io.lb.middleware.common.shared.user.UserData

class SplashDataSource(
    private val userDatabaseService: UserDatabaseService,
) {
    /**
     * Get the user data.
     *
     * @return The user data.
     */
    suspend fun getCurrentUser(): UserData? {
        return userDatabaseService.getCurrentUser()
    }
}
