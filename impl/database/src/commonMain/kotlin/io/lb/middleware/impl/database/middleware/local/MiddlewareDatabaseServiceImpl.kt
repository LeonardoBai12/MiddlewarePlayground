package io.lb.middleware.impl.database.middleware.local

import io.lb.middleware.common.data.middleware.local.MiddlewareDatabaseService
import io.lb.middleware.common.data.middleware.local.model.MappedApiEntity
import io.lb.middleware.common.data.middleware.local.model.MappedRouteEntity
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

    override suspend fun getRoutesHistory(): List<MappedRouteEntity> {
        return queries.getRoutesHistory().executeAsList().map {
            MappedRouteEntity(
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
                preConfiguredBody = it.preConfiguredBody
            )
        }
    }

    override suspend fun getApiHistory(): List<MappedApiEntity> {
        return queries.getApiHistory().executeAsList().map {
            MappedApiEntity(
                uuid = it.uuid,
                baseUrl = it.baseUrl,
                originalApiBaseUrl = it.originalApiBaseUrl
            )
        }
    }

    override suspend fun getRouteBysApiId(routeId: String): List<MappedRouteEntity> {
        return queries.getRouteByApiId(routeId).executeAsList().map {
            MappedRouteEntity(
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
                preConfiguredBody = it.preConfiguredBody
            )
        }
    }

    override suspend fun getRouteById(routeId: String): MappedRouteEntity? {
        return queries.getRouteById(routeId).executeAsOneOrNull()?.let {
            MappedRouteEntity(
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
                preConfiguredBody = it.preConfiguredBody
            )
        }
    }

    override suspend fun getApiById(apiId: String): MappedApiEntity? {
        return queries.getApiById(apiId).executeAsOneOrNull()?.let {
            MappedApiEntity(
                uuid = it.uuid,
                baseUrl = it.baseUrl,
                originalApiBaseUrl = it.originalApiBaseUrl
            )
        }
    }

    override suspend fun saveRoute(route: MappedRouteEntity) {
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

    override suspend fun saveApi(api: MappedApiEntity) {
        queries.saveApi(
            uuid = api.uuid,
            baseUrl = api.baseUrl,
            originalApiBaseUrl = api.originalApiBaseUrl
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
