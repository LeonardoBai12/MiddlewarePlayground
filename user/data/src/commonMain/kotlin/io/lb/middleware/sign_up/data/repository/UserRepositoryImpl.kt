package io.lb.middleware.sign_up.data.repository

import io.lb.middleware.common.shared.user.UserData
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource
import io.lb.middleware.sign_up.data.data_source.UserDataSource
import io.middleware.user.domain.repository.UserRepository

class UserRepositoryImpl(
    private val dataSource: UserDataSource
) : UserRepository {
    override suspend fun getCurrentUser(): CommonFlow<Resource<UserData?>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(data: UserData): CommonFlow<UserData?> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePassword(
        password: String,
        newPassword: String
    ): CommonFlow<Resource<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(userId: String, password: String): CommonFlow<Resource<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): CommonFlow<Resource<Unit>> {
        TODO("Not yet implemented")
    }

}
