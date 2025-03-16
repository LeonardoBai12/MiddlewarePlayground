package io.lb.middleware.sign_up.data.repository

import io.lb.middleware.common.shared.user.UserData
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource
import io.lb.middleware.sign_up.data.data_source.SignUpDataSource
import io.middleware.sign_up.domain.repository.SignUpRepository

class SignUpRepositoryImpl(
    private val dataSource: SignUpDataSource
) : SignUpRepository {
    override suspend fun login(email: String, password: String): CommonFlow<Resource<UserData?>> {
        TODO("Not yet implemented")
    }

    override suspend fun signUp(data: UserData, password: String): CommonFlow<Resource<Unit>> {
        TODO("Not yet implemented")
    }
}
