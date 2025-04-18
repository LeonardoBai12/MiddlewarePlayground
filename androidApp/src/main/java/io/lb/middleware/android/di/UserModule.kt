package io.lb.middleware.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.lb.middleware.common.data.middleware.local.MiddlewareDatabaseService
import io.lb.middleware.common.data.user.local.UserDatabaseService
import io.lb.middleware.common.remote.user.remote.UserClientService
import io.lb.middleware.signup.data.datasource.UserDataSource
import io.lb.middleware.signup.data.repository.UserRepositoryImpl
import io.middleware.user.domain.repository.UserRepository
import io.middleware.user.domain.usecases.DeleteUserUseCase
import io.middleware.user.domain.usecases.GetCurrentUserUseCase
import io.middleware.user.domain.usecases.LogoutUseCase
import io.middleware.user.domain.usecases.UpdatePasswordUseCase
import io.middleware.user.domain.usecases.UpdateUserUseCase

@Module
@InstallIn(ViewModelComponent::class)
object UserModule {
    @Provides
    @ViewModelScoped
    fun provideUserDataSource(
        middlewareDatabaseService: MiddlewareDatabaseService,
        userDatabaseService: UserDatabaseService,
        userClientService: UserClientService
    ): UserDataSource {
        return UserDataSource(
            middlewareDatabaseService,
            userDatabaseService,
            userClientService
        )
    }

    @Provides
    @ViewModelScoped
    fun provideUserRepository(dataSource: UserDataSource): UserRepository {
        return UserRepositoryImpl(dataSource)
    }

    @Provides
    @ViewModelScoped
    fun provideDeleteUserUseCase(repository: UserRepository): DeleteUserUseCase {
        return DeleteUserUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetCurrentUserUseCase(repository: UserRepository): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideLogoutUseCase(repository: UserRepository): LogoutUseCase {
        return LogoutUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideUpdatePasswordUseCase(repository: UserRepository): UpdatePasswordUseCase {
        return UpdatePasswordUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideUpdateUserUseCase(repository: UserRepository): UpdateUserUseCase {
        return UpdateUserUseCase(repository)
    }
}
