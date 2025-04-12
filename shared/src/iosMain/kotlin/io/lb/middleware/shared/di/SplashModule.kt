package io.lb.middleware.shared.di

import io.lb.middleware.splash.data.datasource.SplashDataSource
import io.lb.middleware.splash.data.repository.SplashRepositoryImpl
import io.middleware.splash.domain.repository.SplashRepository
import io.middleware.splash.domain.usecases.GetCurrentUserOnInitUseCase

class SplashModule {
    private val appModule by lazy {
        AppModule()
    }
     private val dataSource by lazy {
         SplashDataSource(appModule.userDatabaseService)
     }
     private val repository: SplashRepository by lazy {
         SplashRepositoryImpl(dataSource)
     }
     val getCurrentUserOnInitUseCase by lazy {
         GetCurrentUserOnInitUseCase(repository)
     }
}
