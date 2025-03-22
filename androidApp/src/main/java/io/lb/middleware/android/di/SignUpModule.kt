@file:OptIn(ExperimentalUuidApi::class)

package io.lb.middleware.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.lb.middleware.common.data.user.local.UserDatabaseService
import io.lb.middleware.common.remote.user.remote.UserClientService
import io.lb.middleware.sign_up.data.data_source.SignUpDataSource
import io.lb.middleware.sign_up.data.repository.SignUpRepositoryImpl
import io.middleware.sign_up.domain.repository.SignUpRepository
import io.middleware.sign_up.domain.use_cases.LoginUseCase
import io.middleware.sign_up.domain.use_cases.SignUpUseCase
import kotlin.uuid.ExperimentalUuidApi

@Module
@InstallIn(ViewModelComponent::class)
object SignUpModule {
    @Provides
    @ViewModelScoped
    fun provideSignUpDataSource(
        userDatabaseService: UserDatabaseService,
        userClientService: UserClientService
    ): SignUpDataSource {
        return SignUpDataSource(
            userDatabaseService,
            userClientService
        )
    }

    @Provides
    @ViewModelScoped
    fun provideSignUpRepository(dataSource: SignUpDataSource): SignUpRepository {
        return SignUpRepositoryImpl(dataSource)
    }

    @Provides
    @ViewModelScoped
    fun provideLoginUseCase(repository: SignUpRepository): LoginUseCase {
        return LoginUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideSignUpUseCase(repository: SignUpRepository): SignUpUseCase {
        return SignUpUseCase(repository)
    }
}
