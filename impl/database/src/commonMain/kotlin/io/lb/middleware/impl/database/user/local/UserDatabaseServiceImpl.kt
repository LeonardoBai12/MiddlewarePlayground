package io.lb.middleware.impl.database.user.local

import io.lb.middleware.common.data.user.local.UserDatabaseService
import io.lb.middleware.common.data.user.local.model.UserEntity
import io.lb.middleware.common.shared.user.UserData

/**
 * Service interface for interacting with the database.
 */
class UserDatabaseServiceImpl : UserDatabaseService {
    override suspend fun saveUser(user: UserData) {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(userId: String): UserData? {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(user: UserData) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(userId: String) {
        TODO("Not yet implemented")
    }
}
