package io.lb.middleware.shared.di

import io.lb.middleware.sign_up.data.data_source.UserDataSource
import io.lb.middleware.sign_up.data.repository.UserRepositoryImpl
import io.middleware.user.domain.repository.UserRepository
import io.middleware.user.domain.use_cases.DeleteUserUseCase
import io.middleware.user.domain.use_cases.GetCurrentUserUseCase
import io.middleware.user.domain.use_cases.LogoutUseCase
import io.middleware.user.domain.use_cases.UpdatePasswordUseCase
import io.middleware.user.domain.use_cases.UpdateUserUseCase

object UserModule {
    private val dataSource by lazy {
        UserDataSource(
            AppModule.middlewareDatabaseService,
            AppModule.userDatabaseService,
            AppModule.userClientService
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
