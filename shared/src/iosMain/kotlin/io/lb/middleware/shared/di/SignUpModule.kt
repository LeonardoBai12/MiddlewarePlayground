@file:OptIn(ExperimentalUuidApi::class)

package io.lb.middleware.shared.di

import io.lb.middleware.sign_up.data.data_source.SignUpDataSource
import io.lb.middleware.sign_up.data.repository.SignUpRepositoryImpl
import io.middleware.sign_up.domain.repository.SignUpRepository
import io.middleware.sign_up.domain.use_cases.LoginUseCase
import io.middleware.sign_up.domain.use_cases.SignUpUseCase
import kotlin.uuid.ExperimentalUuidApi

object SignUpModule {
    private val dataSource by lazy {
        SignUpDataSource(
            AppModule.userDatabaseService,
            AppModule.userClientService,
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
