package io.middleware.api.domain.use_cases

import io.lb.middleware.common.shared.middleware.error.MiddlewareException
import io.lb.middleware.common.shared.middleware.model.PreviewRequest
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource
import io.middleware.api.domain.repository.MiddlewareRepository

class RequestPreviewUseCase(
    private val repository: MiddlewareRepository
) {
    suspend operator fun invoke(data: PreviewRequest): CommonFlow<Resource<String>> {
        if (data.originalResponse.isBlank()) {
            throw MiddlewareException("Original response is empty")
        }
        if (data.mappingRules.newBodyFields.isEmpty()) {
            throw MiddlewareException("Mapping rules are empty")
        }
        if (data.mappingRules.oldBodyFields.isEmpty()) {
            throw MiddlewareException("Mapping rules contain empty fields")
        }
        return repository.requestPreview(data)
    }
}
