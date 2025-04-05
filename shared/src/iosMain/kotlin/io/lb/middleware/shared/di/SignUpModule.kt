package io.lb.middleware.shared.di

import io.lb.middleware.signup.data.datasource.SignUpDataSource
import io.lb.middleware.signup.data.repository.SignUpRepositoryImpl
import io.middleware.signup.domain.repository.SignUpRepository
import io.middleware.signup.domain.usecases.LoginUseCase
import io.middleware.signup.domain.usecases.SignUpUseCase
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class SignUpModule {
    private val appModule by lazy {
        AppModule()
    }
    private val dataSource by lazy {
        SignUpDataSource(
            appModule.userDatabaseService,
            appModule.userClientService,
        )
    }
    private val repository: SignUpRepository by lazy {
        SignUpRepositoryImpl(dataSource)
    }
    val loginUseCase by lazy {
        LoginUseCase(repository)
    }

    val signUpUseCase by lazy {
        SignUpUseCase(repository)
    }
}
