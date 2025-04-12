package io.middleware.api.domain.usecases

import io.lb.middleware.common.shared.middleware.error.MiddlewareException
import io.lb.middleware.common.shared.middleware.model.MappingRules
import io.lb.middleware.common.shared.middleware.model.NewBodyField
import io.lb.middleware.common.shared.middleware.model.OldBodyField
import io.lb.middleware.common.shared.middleware.model.PreviewRequest
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource
import io.middleware.api.domain.repository.MiddlewareRepository

class RequestPreviewUseCase(
    private val repository: MiddlewareRepository
) {
    suspend operator fun invoke(
        originalResponse: String,
        newBodyFields: Map<String, NewBodyField>,
        oldBodyFields: Map<String, OldBodyField>,
        ignoreEmptyValues: Boolean
    ): CommonFlow<Resource<String>> {
        if (originalResponse.isBlank()) {
            throw MiddlewareException("Original response is empty")
        }
        if (newBodyFields.isEmpty()) {
            throw MiddlewareException("Mapping rules are empty")
        }
        if (oldBodyFields.isEmpty()) {
            throw MiddlewareException("Mapping rules contain empty fields")
        }
        return repository.requestPreview(
            PreviewRequest(
                originalResponse = originalResponse,
                mappingRules = MappingRules(
                    newBodyFields = newBodyFields,
                    oldBodyFields = oldBodyFields,
                    ignoreEmptyValues = ignoreEmptyValues
                )
            )
        )
    }
}
