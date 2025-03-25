package io.middleware.api.domain.usecases

import io.lb.middleware.common.shared.middleware.error.MiddlewareException
import io.lb.middleware.common.shared.middleware.model.MappingRequest
import io.lb.middleware.common.shared.middleware.model.MappingRules
import io.lb.middleware.common.shared.middleware.model.NewBodyField
import io.lb.middleware.common.shared.middleware.model.OldBodyField
import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods
import io.lb.middleware.common.shared.util.isValidUrl
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource
import io.middleware.api.domain.repository.MiddlewareRepository

class CreateNewRouteUseCase(
    private val repository: MiddlewareRepository
) {
    suspend operator fun invoke(
        path: String,
        method: MiddlewareHttpMethods,
        originalBaseUrl: String,
        originalPath: String,
        originalMethod: MiddlewareHttpMethods,
        originalQueries: Map<String, String> = mapOf(),
        originalHeaders: Map<String, String> = mapOf(),
        originalBody: String,
        preConfiguredQueries: Map<String, String> = mapOf(),
        preConfiguredHeaders: Map<String, String> = mapOf(),
        preConfiguredBody: String?,
        newBodyFields: Map<String, NewBodyField>,
        oldBodyFields: Map<String, OldBodyField>,
        ignoreEmptyValues: Boolean
    ): CommonFlow<Resource<String>> {
        if (path.isBlank()) {
            throw MiddlewareException("Path is empty")
        }
        if (originalPath.isBlank()) {
            throw MiddlewareException("Original path is empty")
        }
        if (originalBaseUrl.isBlank()) {
            throw MiddlewareException("Original base url is empty")
        }
        if (originalBaseUrl.isValidUrl().not()) {
            throw MiddlewareException("Original base url is not a valid url")
        }
        if (newBodyFields.isEmpty()) {
            throw MiddlewareException("Mapping rules are empty")
        }
        if (oldBodyFields.isEmpty()) {
            throw MiddlewareException("Mapping rules contain empty fields")
        }
        return repository.createNewRoute(
            MappingRequest(
                path = path,
                method = method,
                originalBaseUrl = originalBaseUrl,
                originalPath = originalPath,
                originalMethod = originalMethod,
                originalQueries = originalQueries,
                originalHeaders = originalHeaders,
                originalBody = originalBody,
                preConfiguredQueries = preConfiguredQueries,
                preConfiguredHeaders = preConfiguredHeaders,
                preConfiguredBody = preConfiguredBody,
                mappingRules = MappingRules(
                    newBodyFields = newBodyFields,
                    oldBodyFields = oldBodyFields,
                    ignoreEmptyValues = ignoreEmptyValues
                )
            )
        )
    }
}
