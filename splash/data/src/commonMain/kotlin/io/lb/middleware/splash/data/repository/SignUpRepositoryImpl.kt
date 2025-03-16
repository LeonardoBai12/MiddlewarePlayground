package io.lb.middleware.splash.data.repository

import io.lb.middleware.common.shared.user.UserData
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource
import io.lb.middleware.splash.data.data_source.SplashDataSource
import io.middleware.splash.domain.repository.SignUpRepository

class SignUpRepositoryImpl(
    private val dataSource: SplashDataSource
) : SignUpRepository {
    override suspend fun getCurrentUser(): CommonFlow<Resource<UserData?>> {
        TODO()
    }
}
