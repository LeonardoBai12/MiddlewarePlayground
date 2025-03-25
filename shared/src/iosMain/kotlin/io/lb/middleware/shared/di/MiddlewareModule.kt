package io.lb.middleware.shared.di

import io.lb.middleware.api.data.data_source.MiddlewareDataSource
import io.lb.middleware.api.data.repository.MiddlewareRepositoryImpl
import io.middleware.api.domain.repository.MiddlewareRepository
import io.middleware.api.domain.usecases.CreateNewRouteUseCase
import io.middleware.api.domain.usecases.GetAllRoutesUseCase
import io.middleware.api.domain.usecases.RequestPreviewUseCase

object MiddlewareModule {
    private val dataSource by lazy {
        MiddlewareDataSource(
            AppModule.middlewareDatabaseService,
            AppModule.middlewareClientService,
            AppModule.userDatabaseService,
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
}
