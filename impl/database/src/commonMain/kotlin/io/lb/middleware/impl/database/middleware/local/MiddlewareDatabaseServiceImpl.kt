package io.lb.middleware.impl.database.middleware.local

import io.lb.middleware.common.data.middleware.local.MiddlewareDatabaseService
import io.lb.middleware.common.shared.middleware.model.MappedApi
import io.lb.middleware.common.shared.middleware.model.MappedRoute
import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods
import io.lb.middleware.impl.client.MiddlewareDatabase
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Service interface for interacting with the database.
 */
class MiddlewareDatabaseServiceImpl(
    dataBase: MiddlewareDatabase
) : MiddlewareDatabaseService {
    private val json = Json {
        ignoreUnknownKeys = true
    }
    private val queries = dataBase.middlewareQueries

    override suspend fun getRoutesHistory(): List<MappedRoute> {
        return queries.getRoutesHistory().executeAsList().map {
            MappedRoute(
                uuid = it.uuid,
                path = it.path,
                method = MiddlewareHttpMethods.valueOf(it.method),
                originalBaseUrl = it.originalBaseUrl,
                originalPath = it.originalPath,
                originalMethod = MiddlewareHttpMethods.valueOf(it.originalMethod),
                originalQueries = json.decodeFromString(it.originalQueries),
                originalHeaders = json.decodeFromString(it.originalHeaders),
                originalBody = it.originalBody,
                preConfiguredQueries = json.decodeFromString(it.preConfiguredQueries),
                preConfiguredHeaders = json.decodeFromString(it.preConfiguredHeaders),
                preConfiguredBody = it.preConfiguredBody,
                isFavourite = it.favourite == 1L
            )
        }
    }

    override suspend fun getApiHistory(): List<MappedApi> {
        return queries.getApiHistory().executeAsList().map {
            MappedApi(
                uuid = it.uuid,
                baseUrl = it.baseUrl,
                originalBaseUrl = it.originalApiBaseUrl,
                isFavourite = it.favourite == 1L
            )
        }
    }

    override suspend fun getRoutesByApiId(apiId: String): List<MappedRoute> {
        return queries.getRouteByApiId(apiId).executeAsList().map {
            MappedRoute(
                uuid = it.uuid,
                path = it.path,
                method = MiddlewareHttpMethods.valueOf(it.method),
                originalBaseUrl = it.originalBaseUrl,
                originalPath = it.originalPath,
                originalMethod = MiddlewareHttpMethods.valueOf(it.originalMethod),
                originalQueries = json.decodeFromString(it.originalQueries),
                originalHeaders = json.decodeFromString(it.originalHeaders),
                originalBody = it.originalBody,
                preConfiguredQueries = json.decodeFromString(it.preConfiguredQueries),
                preConfiguredHeaders = json.decodeFromString(it.preConfiguredHeaders),
                preConfiguredBody = it.preConfiguredBody,
                isFavourite = it.favourite == 1L
            )
        }
    }

    override suspend fun getRouteById(routeId: String): MappedRoute? {
        return queries.getRouteById(routeId).executeAsOneOrNull()?.let {
            MappedRoute(
                uuid = it.uuid,
                path = it.path,
                method = MiddlewareHttpMethods.valueOf(it.method),
                originalBaseUrl = it.originalBaseUrl,
                originalPath = it.originalPath,
                originalMethod = MiddlewareHttpMethods.valueOf(it.originalMethod),
                originalQueries = json.decodeFromString(it.originalQueries),
                originalHeaders = json.decodeFromString(it.originalHeaders),
                originalBody = it.originalBody,
                preConfiguredQueries = json.decodeFromString(it.preConfiguredQueries),
                preConfiguredHeaders = json.decodeFromString(it.preConfiguredHeaders),
                preConfiguredBody = it.preConfiguredBody,
                isFavourite = it.favourite == 1L
            )
        }
    }

    override suspend fun getApiByBaseUrl(apiBaseUrl: String): MappedApi? {
        return queries.getApiByBaseUrl(apiBaseUrl).executeAsOneOrNull()?.let {
            MappedApi(
                uuid = it.uuid,
                baseUrl = it.baseUrl,
                originalBaseUrl = it.originalApiBaseUrl,
                isFavourite = it.favourite == 1L
            )
        }
    }

    override suspend fun saveRoute(route: MappedRoute) {
        queries.saveRoute(
            uuid = route.uuid,
            path = route.path,
            method = route.method.name,
            originalBaseUrl = route.originalBaseUrl,
            originalPath = route.originalPath,
            originalMethod = route.originalMethod.name,
            originalQueries = json.encodeToString(route.originalQueries),
            originalHeaders = json.encodeToString(route.originalHeaders),
            originalBody = route.originalBody,
            preConfiguredQueries = json.encodeToString(route.preConfiguredQueries),
            preConfiguredHeaders = json.encodeToString(route.preConfiguredHeaders),
            preConfiguredBody = route.preConfiguredBody
        )
    }

    override suspend fun saveApi(api: MappedApi) {
        queries.saveApi(
            uuid = api.uuid,
            baseUrl = api.baseUrl,
            originalApiBaseUrl = api.originalBaseUrl
        )
    }

    override suspend fun switchRouteToFavourite(routeId: String, isFavourite: Boolean) {
        queries.switchRouteToFavourite(
            uuid = routeId,
            favourite = if (isFavourite) 1 else 0
        )
    }

    override suspend fun switchApiToFavourite(apiId: String, isFavourite: Boolean) {
        queries.switchApiToFavourite(
            uuid = apiId,
            favourite = if (isFavourite) 1 else 0
        )
    }

    override suspend fun deleteRoute(routeId: String) {
        queries.deleteRoute(routeId)
    }

    override suspend fun deleteApi(apiId: String) {
        queries.deleteApi(apiId)
    }

    override suspend fun deleteAllRoutes() {
        queries.deleteAllRoutes()
    }

    override suspend fun deleteAllApis() {
        queries.deleteAllApis()
    }
}
