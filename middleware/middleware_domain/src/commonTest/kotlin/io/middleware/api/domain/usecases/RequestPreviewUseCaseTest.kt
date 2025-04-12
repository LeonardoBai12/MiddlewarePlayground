package io.middleware.api.domain.usecases

import io.lb.middleware.common.shared.middleware.error.MiddlewareException
import io.lb.middleware.common.shared.middleware.model.MappedRoute
import io.lb.middleware.common.shared.middleware.model.MappingRequest
import io.lb.middleware.common.shared.middleware.model.NewBodyField
import io.lb.middleware.common.shared.middleware.model.OldBodyField
import io.lb.middleware.common.shared.middleware.model.PreviewRequest
import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource
import io.lb.middleware.common.state.toCommonFlow
import io.middleware.api.domain.repository.MiddlewareRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class RequestPreviewUseCaseTest {

    private class FakeMiddlewareRepository : MiddlewareRepository {
        var shouldSucceed = true
        var throwableToEmit: Throwable? = null
        var previewResponse = "preview-response"
        var lastPreviewRequest: PreviewRequest? = null

        override suspend fun requestPreview(data: PreviewRequest): CommonFlow<Resource<String>> {
            lastPreviewRequest = data
            return if (shouldSucceed) {
                flowOf(Resource.Success(previewResponse)).toCommonFlow()
            } else {
                val error = throwableToEmit ?: RuntimeException("Preview failed")
                flowOf(Resource.Error<String>(error)).toCommonFlow()
            }
        }

        // Other methods with empty implementations
        override suspend fun requestMappedRoute(
            path: String,
            method: MiddlewareHttpMethods,
            queries: Map<String, String>,
            preConfiguredQueries: Map<String, String>,
            preConfiguredHeaders: Map<String, String>,
            preConfiguredBody: String?
        ): String? = null

        override suspend fun getAllRoutes(): CommonFlow<Resource<List<MappedRoute>>> {
            return flowOf(Resource.Success<List<MappedRoute>>(emptyList())).toCommonFlow()
        }
        override suspend fun saveRouteInHistory(route: MappedRoute) {}
        override suspend fun testOriginalRoute(
            originalBaseUrl: String,
            originalPath: String,
            originalMethod: MiddlewareHttpMethods,
            originalQueries: Map<String, String>,
            originalHeaders: Map<String, String>,
            originalBody: String?
        ): Pair<Int, String?> = Pair(0, null)
        override suspend fun createNewRoute(data: MappingRequest): CommonFlow<Resource<String>> {
            return flowOf(Resource.Success("")).toCommonFlow()
        }
    }

    // Test data
    private val validOriginalResponse = "{}"
    private val validNewBodyFields = mapOf("newField" to NewBodyField("newField", "String"))
    private val validOldBodyFields = mapOf("oldField" to OldBodyField(listOf("oldField"), "String"))

    @Test
    fun `invoke should throw when originalResponse is blank`() = runTest {
        // Given
        val useCase = RequestPreviewUseCase(FakeMiddlewareRepository())

        // When / Then
        assertFailsWith<MiddlewareException> {
            useCase(
                originalResponse = "",
                newBodyFields = validNewBodyFields,
                oldBodyFields = validOldBodyFields,
                ignoreEmptyValues = false
            ).first()
        }.let { exception ->
            assertEquals("Original response is empty", exception.message)
        }
    }

    @Test
    fun `invoke should throw when newBodyFields is empty`() = runTest {
        // Given
        val useCase = RequestPreviewUseCase(FakeMiddlewareRepository())

        // When / Then
        assertFailsWith<MiddlewareException> {
            useCase(
                originalResponse = validOriginalResponse,
                newBodyFields = emptyMap(),
                oldBodyFields = validOldBodyFields,
                ignoreEmptyValues = false
            ).first()
        }.let { exception ->
            assertEquals("Mapping rules are empty", exception.message)
        }
    }

    @Test
    fun `invoke should throw when oldBodyFields is empty`() = runTest {
        // Given
        val useCase = RequestPreviewUseCase(FakeMiddlewareRepository())

        // When / Then
        assertFailsWith<MiddlewareException> {
            useCase(
                originalResponse = validOriginalResponse,
                newBodyFields = validNewBodyFields,
                oldBodyFields = emptyMap(),
                ignoreEmptyValues = false
            ).first()
        }.let { exception ->
            assertEquals("Mapping rules contain empty fields", exception.message)
        }
    }

    @Test
    fun `invoke should call repository with correct parameters`() = runTest {
        // Given
        val fakeRepository = FakeMiddlewareRepository()
        val useCase = RequestPreviewUseCase(fakeRepository)

        // When
        useCase(
            originalResponse = validOriginalResponse,
            newBodyFields = validNewBodyFields,
            oldBodyFields = validOldBodyFields,
            ignoreEmptyValues = true
        ).first()

        // Then
        val request = fakeRepository.lastPreviewRequest
        assertEquals(validOriginalResponse, request?.originalResponse)
        assertEquals(validNewBodyFields, request?.mappingRules?.newBodyFields)
        assertEquals(validOldBodyFields, request?.mappingRules?.oldBodyFields)
        assertEquals(true, request?.mappingRules?.ignoreEmptyValues)
    }

    @Test
    fun `invoke should return success when repository succeeds`() = runTest {
        // Given
        val expectedResponse = "preview-result"
        val fakeRepository = FakeMiddlewareRepository().apply {
            previewResponse = expectedResponse
        }
        val useCase = RequestPreviewUseCase(fakeRepository)

        // When
        val result = useCase(
            originalResponse = validOriginalResponse,
            newBodyFields = validNewBodyFields,
            oldBodyFields = validOldBodyFields,
            ignoreEmptyValues = false
        ).first()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(expectedResponse, (result as Resource.Success).data)
    }

    @Test
    fun `invoke should return error when repository fails`() = runTest {
        // Given
        val expectedError = RuntimeException("Test error")
        val fakeRepository = FakeMiddlewareRepository().apply {
            shouldSucceed = false
            throwableToEmit = expectedError
        }
        val useCase = RequestPreviewUseCase(fakeRepository)

        // When
        val result = useCase(
            originalResponse = validOriginalResponse,
            newBodyFields = validNewBodyFields,
            oldBodyFields = validOldBodyFields,
            ignoreEmptyValues = false
        ).first()

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(expectedError, (result as Resource.Error).throwable)
    }
}
