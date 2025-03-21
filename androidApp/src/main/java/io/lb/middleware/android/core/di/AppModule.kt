package io.lb.middleware.android.core.di

import android.app.Application
import app.cash.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.lb.middleware.common.data.middleware.local.MiddlewareDatabaseService
import io.lb.middleware.common.data.user.local.UserDatabaseService
import io.lb.middleware.common.remote.middleware.remote.MiddlewareClientService
import io.lb.middleware.common.remote.user.remote.UserClientService
import io.lb.middleware.impl.client.MiddlewareDatabase
import io.lb.middleware.impl.client.factory.HttpClientFactory
import io.lb.middleware.impl.client.middleware.remote.MiddlewareClientServiceImpl
import io.lb.middleware.impl.client.user.remote.UserClientServiceImpl
import io.lb.middleware.impl.database.factory.DatabaseDriverFactory
import io.lb.middleware.impl.database.middleware.local.MiddlewareDatabaseServiceImpl
import io.lb.middleware.impl.database.user.local.UserDatabaseServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClientFactory().create()
    }

    @Provides
    @Singleton
    fun provideUserClientService(httpClient: HttpClient): UserClientService {
        return UserClientServiceImpl(httpClient)
    }

    @Provides
    @Singleton
    fun provideMiddlewareClientService(httpClient: HttpClient): MiddlewareClientService {
        return MiddlewareClientServiceImpl(httpClient)
    }

    @Provides
    @Singleton
    fun provideDatabaseDriver(app: Application): SqlDriver {
        return DatabaseDriverFactory(app).create()
    }

    @Provides
    @Singleton
    fun provideMiddlewareDatabase(databaseDriver: SqlDriver): MiddlewareDatabase {
        return MiddlewareDatabase(databaseDriver)
    }

    @Provides
    @Singleton
    fun provideUserDatabaseService(database: MiddlewareDatabase): UserDatabaseService {
        return UserDatabaseServiceImpl(database)
    }

    @Provides
    @Singleton
    fun provideMiddlewareDatabaseService(database: MiddlewareDatabase): MiddlewareDatabaseService {
        return MiddlewareDatabaseServiceImpl(database)
    }
}
