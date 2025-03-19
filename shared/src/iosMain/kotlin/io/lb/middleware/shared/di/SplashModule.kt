package io.lb.middleware.shared.di

import io.lb.middleware.splash.data.data_source.SplashDataSource
import io.lb.middleware.splash.data.repository.SplashRepositoryImpl
import io.middleware.splash.domain.repository.SplashRepository
import io.middleware.splash.domain.use_cases.GetCurrentUserOnInitUseCase

object SplashModule {
     private val dataSource by lazy {
         SplashDataSource(AppModule.userDatabaseService)
     }
     private val repository: SplashRepository by lazy {
         SplashRepositoryImpl(dataSource)
     }
     val getCurrentUserOnInitUseCase by lazy {
         GetCurrentUserOnInitUseCase(repository)
     }
}
