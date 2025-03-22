package io.lb.middleware.android.di

import io.lb.middleware.api.data.data_source.MiddlewareDataSource
import io.lb.middleware.api.data.repository.MiddlewareRepositoryImpl
import io.middleware.api.domain.repository.MiddlewareRepository
import io.middleware.api.domain.use_cases.CreateNewRouteUseCase
import io.middleware.api.domain.use_cases.GetAllRoutesUseCase
import io.middleware.api.domain.use_cases.RequestPreviewUseCase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.lb.middleware.common.data.middleware.local.MiddlewareDatabaseService
import io.lb.middleware.common.data.user.local.UserDatabaseService
import io.lb.middleware.common.remote.middleware.remote.MiddlewareClientService
import io.middleware.api.domain.use_cases.RequestMappedRouteUseCase
import io.middleware.api.domain.use_cases.TestOriginalRouteUseCase

@Module
@InstallIn(ViewModelComponent::class)
object MiddlewareModule {
    @Provides
    @ViewModelScoped
    fun provideMiddlewareDataSource(
        middlewareDatabaseService: MiddlewareDatabaseService,
        middlewareClientService: MiddlewareClientService,
        userDatabaseService: UserDatabaseService
    ): MiddlewareDataSource {
        return MiddlewareDataSource(
            middlewareDatabaseService,
            middlewareClientService,
            userDatabaseService
        )
    }

    @Provides
    @ViewModelScoped
    fun provideMiddlewareRepository(dataSource: MiddlewareDataSource): MiddlewareRepository {
        return MiddlewareRepositoryImpl(dataSource)
    }

    @Provides
    @ViewModelScoped
    fun provideCreateNewRouteUseCase(repository: MiddlewareRepository): CreateNewRouteUseCase {
        return CreateNewRouteUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetAllRoutesUseCase(repository: MiddlewareRepository): GetAllRoutesUseCase {
        return GetAllRoutesUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideRequestPreviewUseCase(repository: MiddlewareRepository): RequestPreviewUseCase {
        return RequestPreviewUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideTestOriginalRouteUseCase(repository: MiddlewareRepository): TestOriginalRouteUseCase {
        return TestOriginalRouteUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideRequestMappedRouteUseCase(repository: MiddlewareRepository): RequestMappedRouteUseCase {
        return RequestMappedRouteUseCase(repository)
    }
}
