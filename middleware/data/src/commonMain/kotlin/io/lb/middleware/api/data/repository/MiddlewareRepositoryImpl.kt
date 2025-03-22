package io.lb.middleware.api.data.repository

import io.lb.middleware.api.data.data_source.MiddlewareDataSource
import io.lb.middleware.common.shared.middleware.model.MappedApi
import io.lb.middleware.common.shared.middleware.model.MappedRoute
import io.lb.middleware.common.shared.middleware.model.MappingRequest
import io.lb.middleware.common.shared.middleware.model.PreviewRequest
import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods
import io.lb.middleware.common.shared.user.UserException
import io.lb.middleware.common.state.Resource
import io.lb.middleware.common.state.toCommonFlow
import io.middleware.api.domain.repository.MiddlewareRepository
import kotlinx.coroutines.flow.flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class MiddlewareRepositoryImpl(
    private val dataSource: MiddlewareDataSource
) : MiddlewareRepository {
    override suspend fun requestMappedRoute(
        path: String,
        method: MiddlewareHttpMethods,
        queries: Map<String, String>,
        preConfiguredQueries: Map<String, String>,
        preConfiguredHeaders: Map<String, String>,
        preConfiguredBody: String?
    ): String? {
        val currentUser = dataSource.getCurrentUser() ?: throw UserException("User not found")
        return dataSource.requestMappedRoute(
            token = currentUser.token ?: "",
            path = path,
            method = method,
            queries = queries,
            preConfiguredQueries = preConfiguredQueries,
            preConfiguredHeaders = preConfiguredHeaders,
            preConfiguredBody = preConfiguredBody
        )
    }

    override suspend fun testOriginalRoute(
        originalBaseUrl: String,
        originalPath: String,
        originalMethod: MiddlewareHttpMethods,
        originalQueries: Map<String, String>,
        originalHeaders: Map<String, String>,
        originalBody: String?
    ): String? {
        return dataSource.testOriginalRoute(
            originalBaseUrl = originalBaseUrl,
            originalPath = originalPath,
            originalMethod = originalMethod,
            originalQueries = originalQueries,
            originalHeaders = originalHeaders,
            originalBody = originalBody
        )
    }

    override suspend fun requestPreview(data: PreviewRequest) = flow<Resource<String>> {
        runCatching {
            val currentUser = dataSource.getCurrentUser() ?: throw UserException("User not found")
            val result = dataSource.requestPreview(
                token = currentUser.token ?: throw UserException("User token not found"),
                data = data
            )
            emit(Resource.Success(result))
        }.getOrElse {
            emit(Resource.Error(it))
        }
    }.toCommonFlow()

    override suspend fun createNewRoute(data: MappingRequest) = flow<Resource<String>> {
        runCatching {
            val currentUser = dataSource.getCurrentUser()
            val resultPath = dataSource.createNewRoute(
                token = currentUser?.token ?: throw UserException("User not found"),
                data = data
            )
            val uuid = resultPath.split("/")[1]
            val mappedRoute = MappedRoute(
                uuid = uuid,
                path = resultPath,
                method = data.method,
                originalBaseUrl = data.originalBaseUrl,
                originalPath = data.originalPath,
                originalMethod = data.originalMethod,
                originalQueries = data.originalQueries,
                originalHeaders = data.originalHeaders,
                originalBody = data.originalBody,
                preConfiguredQueries = data.preConfiguredQueries,
                preConfiguredHeaders = data.preConfiguredHeaders,
                preConfiguredBody = data.preConfiguredBody,
                isFavourite = false
            )
            dataSource.getApiByBaseUrl(data.originalBaseUrl) ?: dataSource.saveApi(
                MappedApi(
                    uuid = Uuid.random().toString(),
                    baseUrl = data.originalBaseUrl,
                    originalBaseUrl = data.originalBaseUrl,
                    isFavourite = false
                )
            )
            dataSource.saveRoute(mappedRoute)
            emit(Resource.Success(uuid))
        }.getOrElse {
            emit(Resource.Error(it))
        }
    }.toCommonFlow()

    override suspend fun getAllRoutes() = flow<Resource<List<MappedRoute>>> {
        runCatching {
            val currentUser = dataSource.getCurrentUser()
            val routes = dataSource.getAllRoutes(
                token = currentUser?.token ?: throw UserException("User not found")
            ).map {
                MappedRoute(
                    uuid = it.uuid,
                    path = it.path,
                    method = it.method,
                    originalBaseUrl = it.originalBaseUrl,
                    originalPath = it.originalPath,
                    originalMethod = it.originalMethod,
                    originalQueries = it.originalQueries,
                    originalHeaders = it.originalHeaders,
                    originalBody = it.originalBody,
                    preConfiguredQueries = it.preConfiguredQueries,
                    preConfiguredHeaders = it.preConfiguredHeaders,
                    preConfiguredBody = it.preConfiguredBody,
                    isFavourite = false
                )
            }
            emit(Resource.Success(routes))
        }.getOrElse {
            emit(Resource.Error(it))
        }
    }.toCommonFlow()
}
