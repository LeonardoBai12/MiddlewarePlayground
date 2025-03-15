package io.lb.middleware.common.data.user.local

import io.lb.middleware.common.data.user.local.model.UserEntity

/**
 * Service interface for interacting with the database.
 */
interface UserDatabaseService {
    suspend fun saveUser(user: UserEntity)
    suspend fun getUser(): UserEntity?
    suspend fun updateUser(user: UserEntity)
    suspend fun deleteUser(userId: String)
}
