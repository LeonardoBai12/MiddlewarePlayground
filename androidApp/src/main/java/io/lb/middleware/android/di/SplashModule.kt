package io.lb.middleware.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.lb.middleware.common.data.user.local.UserDatabaseService
import io.lb.middleware.splash.data.data_source.SplashDataSource
import io.lb.middleware.splash.data.repository.SplashRepositoryImpl
import io.middleware.splash.domain.repository.SplashRepository
import io.middleware.splash.domain.use_cases.GetCurrentUserOnInitUseCase

@Module
@InstallIn(ViewModelComponent::class)
object SplashModule {
    @Provides
    @ViewModelScoped
    fun provideSplashDataSource(userDatabaseService: UserDatabaseService): SplashDataSource {
        return SplashDataSource(userDatabaseService)
    }

    @Provides
    @ViewModelScoped
    fun provideSplashRepository(dataSource: SplashDataSource): SplashRepository {
        return SplashRepositoryImpl(dataSource)
    }

    @Provides
    @ViewModelScoped
    fun provideGetCurrentUserOnInitUseCase(repository: SplashRepository): GetCurrentUserOnInitUseCase {
        return GetCurrentUserOnInitUseCase(repository)
    }
}