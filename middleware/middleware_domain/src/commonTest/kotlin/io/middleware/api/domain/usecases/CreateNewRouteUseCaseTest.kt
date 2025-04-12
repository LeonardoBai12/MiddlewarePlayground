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

class CreateNewRouteUseCaseTest {
    private class FakeMiddlewareRepository : MiddlewareRepository {
        var shouldSucceed = true
        var throwableToEmit: Throwable? = null
        var lastMappingRequest: MappingRequest? = null
        var routeIdToReturn = "generated-route-id"
        override suspend fun requestMappedRoute(
            path: String,
            method: MiddlewareHttpMethods,
            queries: Map<String, String>,
            preConfiguredQueries: Map<String, String>,
            preConfiguredHeaders: Map<String, String>,
            preConfiguredBody: String?
        ): String? {
            throw NotImplementedError()
        }

        override suspend fun saveRouteInHistory(route: MappedRoute) {
            throw NotImplementedError()
        }

        override suspend fun testOriginalRoute(
            originalBaseUrl: String,
            originalPath: String,
            originalMethod: MiddlewareHttpMethods,
            originalQueries: Map<String, String>,
            originalHeaders: Map<String, String>,
            originalBody: String?
        ): Pair<Int, String?> {
            throw NotImplementedError()
        }

        override suspend fun requestPreview(data: PreviewRequest): CommonFlow<Resource<String>> {
            throw NotImplementedError()
        }

        override suspend fun createNewRoute(request: MappingRequest): CommonFlow<Resource<String>> {
            lastMappingRequest = request
            return if (shouldSucceed) {
                flowOf(Resource.Success(routeIdToReturn)).toCommonFlow()
            } else {
                val error = throwableToEmit ?: RuntimeException("Create route failed")
                flowOf(Resource.Error<String>(error)).toCommonFlow()
            }
        }

        override suspend fun getAllRoutes(): CommonFlow<Resource<List<MappedRoute>>> {
            throw NotImplementedError()
        }
    }

    // Test data
    private val validNewBodyFields = mapOf(
        "newField" to NewBodyField("newField", "String")
    )
    private val validOldBodyFields = mapOf(
        "oldField" to OldBodyField(listOf("oldField"), "String")
    )
    private val validPath = "/api/test"
    private val validOriginalBaseUrl = "https://api.example.com"
    private val validOriginalPath = "/original"
    private val validOriginalBody = "{}"

    @Test
    fun `invoke should throw when originalBaseUrl is blank`() = runTest {
        // Given
        val useCase = CreateNewRouteUseCase(FakeMiddlewareRepository())

        // When / Then
        assertFailsWith<MiddlewareException> {
            useCase(
                path = validPath,
                method = MiddlewareHttpMethods.Post,
                originalBaseUrl = "",
                originalPath = validOriginalPath,
                originalMethod = MiddlewareHttpMethods.Post,
                originalBody = validOriginalBody,
                newBodyFields = validNewBodyFields,
                oldBodyFields = validOldBodyFields,
                preConfiguredBody = "{}",
                ignoreEmptyValues = false
            ).first()
        }.let { exception ->
            assertEquals("Original base url is empty", exception.message)
        }
    }

    @Test
    fun `invoke should throw when originalBaseUrl is invalid`() = runTest {
        // Given
        val useCase = CreateNewRouteUseCase(FakeMiddlewareRepository())

        // When / Then
        assertFailsWith<MiddlewareException> {
            useCase(
                path = validPath,
                method = MiddlewareHttpMethods.Post,
                originalBaseUrl = "invalid-url",
                originalPath = validOriginalPath,
                originalMethod = MiddlewareHttpMethods.Post,
                originalBody = validOriginalBody,
                newBodyFields = validNewBodyFields,
                oldBodyFields = validOldBodyFields,
                preConfiguredBody = "{}",
                ignoreEmptyValues = false
            ).first()
        }.let { exception ->
            assertEquals("Original base url is not a valid url", exception.message)
        }
    }

    @Test
    fun `invoke should throw when path is blank`() = runTest {
        // Given
        val useCase = CreateNewRouteUseCase(FakeMiddlewareRepository())

        // When / Then
        assertFailsWith<MiddlewareException> {
            useCase(
                path = "",
                method = MiddlewareHttpMethods.Post,
                originalBaseUrl = validOriginalBaseUrl,
                originalPath = validOriginalPath,
                originalMethod = MiddlewareHttpMethods.Post,
                originalBody = validOriginalBody,
                newBodyFields = validNewBodyFields,
                oldBodyFields = validOldBodyFields,
                preConfiguredBody = "{}",
                ignoreEmptyValues = false
            ).first()
        }.let { exception ->
            assertEquals("Path is empty", exception.message)
        }
    }

    @Test
    fun `invoke should throw when originalPath is blank`() = runTest {
        // Given
        val useCase = CreateNewRouteUseCase(FakeMiddlewareRepository())

        // When / Then
        assertFailsWith<MiddlewareException> {
            useCase(
                path = validPath,
                method = MiddlewareHttpMethods.Post,
                originalBaseUrl = validOriginalBaseUrl,
                originalPath = "",
                originalMethod = MiddlewareHttpMethods.Post,
                originalBody = validOriginalBody,
                newBodyFields = validNewBodyFields,
                oldBodyFields = validOldBodyFields,
                preConfiguredBody = "{}",
                ignoreEmptyValues = false
            ).first()
        }.let { exception ->
            assertEquals("Original path is empty", exception.message)
        }
    }

    @Test
    fun `invoke should throw when newBodyFields is empty`() = runTest {
        // Given
        val useCase = CreateNewRouteUseCase(FakeMiddlewareRepository())

        // When / Then
        assertFailsWith<MiddlewareException> {
            useCase(
                path = validPath,
                method = MiddlewareHttpMethods.Post,
                originalBaseUrl = validOriginalBaseUrl,
                originalPath = validOriginalPath,
                originalMethod = MiddlewareHttpMethods.Post,
                originalBody = validOriginalBody,
                newBodyFields = emptyMap(),
                oldBodyFields = validOldBodyFields,
                preConfiguredBody = "{}",
                ignoreEmptyValues = false
            ).first()
        }.let { exception ->
            assertEquals("Mapping rules are empty", exception.message)
        }
    }

    @Test
    fun `invoke should throw when oldBodyFields is empty`() = runTest {
        // Given
        val useCase = CreateNewRouteUseCase(FakeMiddlewareRepository())

        // When / Then
        assertFailsWith<MiddlewareException> {
            useCase(
                path = validPath,
                method = MiddlewareHttpMethods.Post,
                originalBaseUrl = validOriginalBaseUrl,
                originalPath = validOriginalPath,
                originalMethod = MiddlewareHttpMethods.Post,
                originalBody = validOriginalBody,
                newBodyFields = validNewBodyFields,
                oldBodyFields = emptyMap(),
                preConfiguredBody = "{}",
                ignoreEmptyValues = false
            ).first()
        }.let { exception ->
            assertEquals("Mapping rules contain empty fields", exception.message)
        }
    }

    @Test
    fun `invoke should call repository with correct parameters when validation passes`() = runTest {
        // Given
        val fakeRepository = FakeMiddlewareRepository()
        val useCase = CreateNewRouteUseCase(fakeRepository)
        val testPreConfiguredQueries = mapOf("query" to "value")
        val testPreConfiguredHeaders = mapOf("header" to "value")
        val testPreConfiguredBody = "pre-configured-body"

        // When
        useCase(
            path = validPath,
            method = MiddlewareHttpMethods.Post,
            originalBaseUrl = validOriginalBaseUrl,
            originalPath = validOriginalPath,
            originalMethod = MiddlewareHttpMethods.Post,
            originalQueries = mapOf("q" to "test"),
            originalHeaders = mapOf("auth" to "token"),
            originalBody = validOriginalBody,
            preConfiguredQueries = testPreConfiguredQueries,
            preConfiguredHeaders = testPreConfiguredHeaders,
            preConfiguredBody = testPreConfiguredBody,
            newBodyFields = validNewBodyFields,
            oldBodyFields = validOldBodyFields,
            ignoreEmptyValues = true
        ).first()

        // Then
        val request = fakeRepository.lastMappingRequest
        assertEquals(validPath, request?.path)
        assertEquals(MiddlewareHttpMethods.Post, request?.method)
        assertEquals(validOriginalBaseUrl, request?.originalBaseUrl)
        assertEquals(validOriginalPath, request?.originalPath)
        assertEquals(MiddlewareHttpMethods.Post, request?.originalMethod)
        assertEquals(mapOf("q" to "test"), request?.originalQueries)
        assertEquals(mapOf("auth" to "token"), request?.originalHeaders)
        assertEquals(validOriginalBody, request?.originalBody)
        assertEquals(testPreConfiguredQueries, request?.preConfiguredQueries)
        assertEquals(testPreConfiguredHeaders, request?.preConfiguredHeaders)
        assertEquals(testPreConfiguredBody, request?.preConfiguredBody)
        assertEquals(validNewBodyFields, request?.mappingRules?.newBodyFields)
        assertEquals(validOldBodyFields, request?.mappingRules?.oldBodyFields)
        assertEquals(true, request?.mappingRules?.ignoreEmptyValues)
    }

    @Test
    fun `invoke should return success with route ID when repository succeeds`() = runTest {
        // Given
        val expectedRouteId = "test-route-id"
        val fakeRepository = FakeMiddlewareRepository().apply {
            shouldSucceed = true
            routeIdToReturn = expectedRouteId
        }
        val useCase = CreateNewRouteUseCase(fakeRepository)

        // When
        val result = useCase(
            path = validPath,
            method = MiddlewareHttpMethods.Post,
            originalBaseUrl = validOriginalBaseUrl,
            originalPath = validOriginalPath,
            originalMethod = MiddlewareHttpMethods.Post,
            originalBody = validOriginalBody,
            newBodyFields = validNewBodyFields,
            oldBodyFields = validOldBodyFields,
            preConfiguredBody = "{}",
            ignoreEmptyValues = false
        ).first()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(expectedRouteId, (result as Resource.Success).data)
    }

    @Test
    fun `invoke should return error when repository fails`() = runTest {
        // Given
        val expectedError = RuntimeException("Test error")
        val fakeRepository = FakeMiddlewareRepository().apply {
            shouldSucceed = false
            throwableToEmit = expectedError
        }
        val useCase = CreateNewRouteUseCase(fakeRepository)

        // When
        val result = useCase(
            path = validPath,
            method = MiddlewareHttpMethods.Post,
            originalBaseUrl = validOriginalBaseUrl,
            originalPath = validOriginalPath,
            originalMethod = MiddlewareHttpMethods.Post,
            originalBody = validOriginalBody,
            newBodyFields = validNewBodyFields,
            oldBodyFields = validOldBodyFields,
            preConfiguredBody = "{}",
            ignoreEmptyValues = false
        ).first()

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(expectedError, (result as Resource.Error).throwable)
    }

    @Test
    fun `invoke should work with empty optional parameters`() = runTest {
        // Given
        val fakeRepository = FakeMiddlewareRepository()
        val useCase = CreateNewRouteUseCase(fakeRepository)

        // When
        useCase(
            path = validPath,
            method = MiddlewareHttpMethods.Post,
            originalBaseUrl = validOriginalBaseUrl,
            originalPath = validOriginalPath,
            originalMethod = MiddlewareHttpMethods.Post,
            originalBody = validOriginalBody,
            newBodyFields = validNewBodyFields,
            oldBodyFields = validOldBodyFields,
            preConfiguredBody = null,
            ignoreEmptyValues = false
        ).first()

        // Then
        val request = fakeRepository.lastMappingRequest
        assertEquals(emptyMap(), request?.originalQueries)
        assertEquals(emptyMap(), request?.originalHeaders)
        assertEquals(emptyMap(), request?.preConfiguredQueries)
        assertEquals(emptyMap(), request?.preConfiguredHeaders)
        assertEquals(null, request?.preConfiguredBody)
    }
}
