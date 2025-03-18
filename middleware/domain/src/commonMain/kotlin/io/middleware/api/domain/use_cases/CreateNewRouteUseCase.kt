package io.middleware.api.domain.use_cases

import io.lb.middleware.common.shared.middleware.error.MiddlewareException
import io.lb.middleware.common.shared.middleware.model.MappingRequest
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource
import io.middleware.api.domain.repository.MiddlewareRepository

class CreateNewRouteUseCase(
    private val repository: MiddlewareRepository
) {
    suspend operator fun invoke(data: MappingRequest): CommonFlow<Resource<String>> {
        if (data.path.isBlank()) {
            throw MiddlewareException("Path is empty")
        }
        if (data.originalPath.isBlank()) {
            throw MiddlewareException("Original path is empty")
        }
        if (data.originalBaseUrl.isBlank()) {
            throw MiddlewareException("Original base url is empty")
        }
        if (data.mappingRules.newBodyFields.isEmpty()) {
            throw MiddlewareException("Mapping rules are empty")
        }
        if (data.mappingRules.oldBodyFields.isEmpty()) {
            throw MiddlewareException("Mapping rules contain empty fields")
        }
        return repository.createNewRoute(data)
    }
}
