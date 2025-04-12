package io.lb.middleware.splash.data.repository

import io.lb.middleware.common.shared.user.UserData
import io.lb.middleware.splash.data.datasource.SplashDataSource
import io.middleware.splash.domain.repository.SplashRepository

class SplashRepositoryImpl(
    private val dataSource: SplashDataSource
) : SplashRepository {
    override suspend fun getCurrentUser(): UserData? {
        return dataSource.getCurrentUser()
    }
}
