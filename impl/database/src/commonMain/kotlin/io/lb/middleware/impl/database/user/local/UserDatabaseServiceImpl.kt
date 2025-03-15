package io.lb.middleware.impl.client.user.local

import io.lb.middleware.common.data.user.local.UserDatabaseService
import io.lb.middleware.common.data.user.local.model.UserEntity

/**
 * Service interface for interacting with the database.
 */
class UserDatabaseServiceImpl : UserDatabaseService {
    override suspend fun saveUser(user: UserEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(): UserEntity? {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(user: UserEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(userId: String) {
        TODO("Not yet implemented")
    }
}
