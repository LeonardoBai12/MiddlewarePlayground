package io.lb.middleware.android.history.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.lb.middleware.common.data.middleware.local.MiddlewareDatabaseService
import io.lb.middleware.history.data.data_source.HistoryDataSource
import io.lb.middleware.history.data.repository.HistoryRepositoryImpl
import io.middleware.domain.history.repository.HistoryRepository
import io.middleware.domain.history.use_cases.DeleteApiFromHistoryUseCase
import io.middleware.domain.history.use_cases.DeleteRouteFromHistoryUseCase
import io.middleware.domain.history.use_cases.GetApiByBaseUrlUseCase
import io.middleware.domain.history.use_cases.GetApiHistoryUseCase
import io.middleware.domain.history.use_cases.GetRouteByIdUseCase
import io.middleware.domain.history.use_cases.GetRoutesByApiIdUseCase
import io.middleware.domain.history.use_cases.GetRoutesHistoryUseCase
import io.middleware.domain.history.use_cases.SwitchApiToFavouriteUseCase
import io.middleware.domain.history.use_cases.SwitchRouteToFavouriteUseCase
import io.middleware.domain.history.use_cases.WipeDataUseCase

@Module
@InstallIn(ViewModelComponent::class)
object HistoryModule {
    @Provides
    @ViewModelScoped
    fun provideHistoryDataSource(middlewareDatabaseService: MiddlewareDatabaseService): HistoryDataSource {
        return HistoryDataSource(middlewareDatabaseService)
    }

    @Provides
    @ViewModelScoped
    fun provideHistoryRepository(dataSource: HistoryDataSource): HistoryRepository {
        return HistoryRepositoryImpl(dataSource)
    }

    @Provides
    @ViewModelScoped
    fun provideDeleteApiFromHistoryUseCase(repository: HistoryRepository): DeleteApiFromHistoryUseCase {
        return DeleteApiFromHistoryUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideDeleteRouteFromHistoryUseCase(repository: HistoryRepository): DeleteRouteFromHistoryUseCase {
        return DeleteRouteFromHistoryUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetApiByBaseUrlUseCase(repository: HistoryRepository): GetApiByBaseUrlUseCase {
        return GetApiByBaseUrlUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetApiHistoryUseCase(repository: HistoryRepository): GetApiHistoryUseCase {
        return GetApiHistoryUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetRouteByIdUseCase(repository: HistoryRepository): GetRouteByIdUseCase {
        return GetRouteByIdUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetRoutesByApiIdUseCase(repository: HistoryRepository): GetRoutesByApiIdUseCase {
        return GetRoutesByApiIdUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetRoutesHistoryUseCase(repository: HistoryRepository): GetRoutesHistoryUseCase {
        return GetRoutesHistoryUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideSwitchApiToFavouriteUseCase(repository: HistoryRepository): SwitchApiToFavouriteUseCase {
        return SwitchApiToFavouriteUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideSwitchRouteToFavouriteUseCase(repository: HistoryRepository): SwitchRouteToFavouriteUseCase {
        return SwitchRouteToFavouriteUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideWipeDataUseCase(repository: HistoryRepository): WipeDataUseCase {
        return WipeDataUseCase(repository)
    }
}
