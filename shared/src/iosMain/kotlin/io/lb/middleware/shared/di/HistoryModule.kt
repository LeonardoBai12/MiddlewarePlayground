package io.lb.middleware.shared.di

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

object HistoryModule {
    private val dataSource by lazy {
        HistoryDataSource(AppModule.middlewareDatabaseService)
    }
    private val repository: HistoryRepository by lazy {
        HistoryRepositoryImpl(dataSource)
    }

    val deleteApiFromHistoryUseCase by lazy {
        DeleteApiFromHistoryUseCase(repository)
    }
    val deleteRouteFromHistoryUseCase by lazy {
        DeleteRouteFromHistoryUseCase(repository)
    }
    val getApiByBaseUrlUseCase by lazy {
        GetApiByBaseUrlUseCase(repository)
    }
    val getApiHistoryUseCase by lazy {
        GetApiHistoryUseCase(repository)
    }
    val getRouteByIdUseCase by lazy {
        GetRouteByIdUseCase(repository)
    }
    val getRoutesByApiIdUseCase by lazy {
        GetRoutesByApiIdUseCase(repository)
    }
    val getRoutesHistoryUseCase by lazy {
        GetRoutesHistoryUseCase(repository)
    }
    val switchApiToFavouriteUseCase by lazy {
        SwitchApiToFavouriteUseCase(repository)
    }
    val switchRouteToFavouriteUseCase by lazy {
        SwitchRouteToFavouriteUseCase(repository)
    }
    val wipeDataUseCase by lazy {
        WipeDataUseCase(repository)
    }
}
