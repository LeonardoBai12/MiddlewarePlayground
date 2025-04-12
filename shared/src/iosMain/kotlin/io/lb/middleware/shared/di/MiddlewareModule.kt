package io.lb.middleware.shared.di

import io.lb.middleware.api.data.datasource.MiddlewareDataSource
import io.lb.middleware.api.data.repository.MiddlewareRepositoryImpl
import io.middleware.api.domain.repository.MiddlewareRepository
import io.middleware.api.domain.usecases.CreateNewRouteUseCase
import io.middleware.api.domain.usecases.GetAllRoutesUseCase
import io.middleware.api.domain.usecases.RequestMappedRouteUseCase
import io.middleware.api.domain.usecases.RequestPreviewUseCase
import io.middleware.api.domain.usecases.SaveRouteInHistoryUseCase
import io.middleware.api.domain.usecases.TestOriginalRouteUseCase

class MiddlewareModule {
    private val appModule by lazy {
        AppModule()
    }
    private val dataSource by lazy {
        MiddlewareDataSource(
            appModule.middlewareDatabaseService,
            appModule.middlewareClientService,
            appModule.userDatabaseService,
        )
    }
    private val repository: MiddlewareRepository by lazy {
        MiddlewareRepositoryImpl(dataSource)
    }
    val createNewRouteUseCase by lazy {
        CreateNewRouteUseCase(repository)
    }
    val getAllRoutesUseCase by lazy {
        GetAllRoutesUseCase(repository)
    }
    val requestPreviewUseCase by lazy {
        RequestPreviewUseCase(repository)
    }
    val testOriginalRouteUseCase by lazy {
        TestOriginalRouteUseCase(repository)
    }
    val requestMappedRouteUseCase by lazy {
        RequestMappedRouteUseCase(repository)
    }
    val saveRouteInHistoryUseCase by lazy {
        SaveRouteInHistoryUseCase(repository)
    }
}
