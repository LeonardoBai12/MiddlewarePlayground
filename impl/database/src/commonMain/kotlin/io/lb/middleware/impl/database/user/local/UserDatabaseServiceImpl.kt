package io.lb.middleware.impl.database.user.local

import io.lb.middleware.common.data.user.local.UserDatabaseService
import io.lb.middleware.common.shared.user.UserData
import io.lb.middleware.impl.client.MiddlewareDatabase

/**
 * Service interface for interacting with the database.
 */
class UserDatabaseServiceImpl(
    database: MiddlewareDatabase
) : UserDatabaseService {
    private val queries = database.middlewareQueries

    override suspend fun saveUser(user: UserData) {
        queries.insertUser(
            token = user.token ?: "",
            userId = user.userId,
            userName = user.userName,
            phone = user.phone,
            email = user.email,
            profilePictureUrl = user.profilePictureUrl
        )
    }

    override suspend fun getCurrentUser(): UserData? {
        return queries.getUser().executeAsOneOrNull()?.let {
            UserData(
                token = it.token,
                userId = it.userId,
                userName = it.userName,
                phone = it.phone,
                email = it.email,
                profilePictureUrl = it.profilePictureUrl
            )
        }
    }

    override suspend fun updateUser(user: UserData) {
        queries.updateUser(
            token = user.token ?: "",
            userId = user.userId,
            userName = user.userName,
            phone = user.phone,
            email = user.email,
            profilePictureUrl = user.profilePictureUrl
        )
    }

    override suspend fun deleteUser(userId: String) {
        queries.deleteUser(userId)
    }
}
