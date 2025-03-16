package io.lb.middleware.api.data.repository

import io.lb.middleware.api.data.data_source.MiddlewareDataSource
import io.lb.middleware.common.shared.middleware.model.MappedRoute
import io.lb.middleware.common.shared.middleware.model.MappingRequest
import io.lb.middleware.common.shared.middleware.model.PreviewRequest
import io.lb.middleware.common.shared.user.UserData
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource
import io.middleware.api.domain.repository.MiddlewareRepository

class MiddlewareRepositoryImpl(
    private val dataSource: MiddlewareDataSource
) : MiddlewareRepository {
    override suspend fun getCurrentUser(): UserData? {
        TODO("Not yet implemented")
    }

    override suspend fun requestPreview(data: PreviewRequest): CommonFlow<Resource<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun createNewRoute(data: MappingRequest): CommonFlow<Resource<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllRoutes(): CommonFlow<Resource<List<MappedRoute>>> {
        TODO("Not yet implemented")
    }
}
