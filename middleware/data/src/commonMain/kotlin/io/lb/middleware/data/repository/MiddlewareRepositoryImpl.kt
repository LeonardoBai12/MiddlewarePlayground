package io.lb.middleware.data.repository

import io.lb.middleware.common.shared.middleware.model.MappedApi
import io.lb.middleware.common.shared.middleware.model.MappedRoute
import io.lb.middleware.common.shared.middleware.model.MappingRequest
import io.lb.middleware.common.shared.middleware.model.PreviewRequest
import io.lb.middleware.common.shared.user.UserData
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource
import io.lb.middleware.data.data_source.MiddlewareDataSource
import io.middleware.domain.repository.MiddlewareRepository

class MiddlewareRepositoryImpl(
    private val dataSource: MiddlewareDataSource
) : MiddlewareRepository {
    override suspend fun getRoutesHistory(): CommonFlow<Resource<List<MappedRoute>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getApiHistory(): CommonFlow<Resource<List<MappedApi>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRoutesByApiId(apiId: String): CommonFlow<Resource<List<MappedRoute>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRouteById(routeId: String): CommonFlow<Resource<MappedRoute?>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRouteFromHistory(routeId: String): CommonFlow<Resource<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteApi(apiId: String): CommonFlow<Resource<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllRoutes(): CommonFlow<Resource<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllApis(): CommonFlow<Resource<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun login(email: String, password: String): CommonFlow<UserData?> {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentUser(): CommonFlow<UserData?> {
        TODO("Not yet implemented")
    }

    override suspend fun signUp(data: UserData): CommonFlow<Resource<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(data: UserData): CommonFlow<UserData?> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePassword(
        password: String,
        newPassword: String
    ): CommonFlow<Resource<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(userId: String, password: String): CommonFlow<Resource<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): CommonFlow<Resource<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun requestPreview(data: PreviewRequest): CommonFlow<String> {
        TODO("Not yet implemented")
    }

    override suspend fun createNewRoute(data: MappingRequest): CommonFlow<Resource<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllRoutes(): CommonFlow<Resource<List<MappedRoute>>> {
        TODO("Not yet implemented")
    }

}
