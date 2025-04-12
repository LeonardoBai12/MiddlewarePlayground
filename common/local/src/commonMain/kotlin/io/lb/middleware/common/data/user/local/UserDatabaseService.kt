package io.lb.middleware.common.data.user.local

import io.lb.middleware.common.shared.user.UserData

/**
 * Service interface for interacting with the database.
 */
interface UserDatabaseService {
    suspend fun saveUser(user: UserData)
    suspend fun getCurrentUser(): UserData?
    suspend fun updateUser(user: UserData)
    suspend fun deleteUser(userId: String)
}
