package io.middleware.api.domain.usecases

import io.lb.middleware.common.shared.middleware.model.MappedRoute
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource
import io.middleware.api.domain.repository.MiddlewareRepository

class GetAllRoutesUseCase(
    private val repository: MiddlewareRepository
) {
    suspend operator fun invoke(): CommonFlow<Resource<List<MappedRoute>>> {
        return repository.getAllRoutes()
    }
}
