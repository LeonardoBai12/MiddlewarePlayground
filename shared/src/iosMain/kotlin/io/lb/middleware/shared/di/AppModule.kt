package io.lb.middleware.shared.di

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

class AppModule {
    private val httpClient by lazy {
        HttpClientFactory().create()
    }
    internal val userClientService: UserClientService by lazy {
        UserClientServiceImpl(httpClient)
    }
    internal val middlewareClientService: MiddlewareClientService by lazy {
        MiddlewareClientServiceImpl(httpClient)
    }

    private val databaseDriver by lazy {
        DatabaseDriverFactory().create()
    }
    private val database by lazy {
        MiddlewareDatabase(databaseDriver)
    }
    internal val userDatabaseService: UserDatabaseService by lazy {
        UserDatabaseServiceImpl(database)
    }
    internal val middlewareDatabaseService: MiddlewareDatabaseService by lazy {
        MiddlewareDatabaseServiceImpl(database)
    }
}
