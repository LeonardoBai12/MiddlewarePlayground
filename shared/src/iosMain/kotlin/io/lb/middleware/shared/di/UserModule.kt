package io.lb.middleware.shared.di

import io.lb.middleware.signup.data.datasource.UserDataSource
import io.lb.middleware.signup.data.repository.UserRepositoryImpl
import io.middleware.user.domain.repository.UserRepository
import io.middleware.user.domain.usecases.DeleteUserUseCase
import io.middleware.user.domain.usecases.GetCurrentUserUseCase
import io.middleware.user.domain.usecases.LogoutUseCase
import io.middleware.user.domain.usecases.UpdatePasswordUseCase
import io.middleware.user.domain.usecases.UpdateUserUseCase

class UserModule {
    private val appModule by lazy {
        AppModule()
    }
    private val dataSource by lazy {
        UserDataSource(
            appModule.middlewareDatabaseService,
            appModule.userDatabaseService,
            appModule.userClientService
        )
    }
    private val repository: UserRepository by lazy {
        UserRepositoryImpl(dataSource)
    }
    val deleteUserUseCase by lazy {
        DeleteUserUseCase(repository)
    }
    val getCurrentUserUseCase by lazy {
        GetCurrentUserUseCase(repository)
    }
    val logoutUseCase by lazy {
        LogoutUseCase(repository)
    }
    val updatePasswordUseCase by lazy {
        UpdatePasswordUseCase(repository)
    }
    val updateUserUseCase by lazy {
        UpdateUserUseCase(repository)
    }
}
