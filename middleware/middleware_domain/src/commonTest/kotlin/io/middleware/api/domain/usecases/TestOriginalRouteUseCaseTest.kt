package io.middleware.api.domain.usecases

import io.lb.middleware.common.shared.middleware.error.MiddlewareException
import io.lb.middleware.common.shared.middleware.model.MappedRoute
import io.lb.middleware.common.shared.middleware.model.MappingRequest
import io.lb.middleware.common.shared.middleware.model.PreviewRequest
import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource
import io.lb.middleware.common.state.toCommonFlow
import io.middleware.api.domain.repository.MiddlewareRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TestOriginalRouteUseCaseTest {

    private class FakeMiddlewareRepository : MiddlewareRepository {
        var responseToReturn: Pair<Int, String?> = Pair(200, "response")
        var exceptionToThrow: Throwable? = null
        var lastTestRequest: TestRequestParams? = null

        data class TestRequestParams(
            val baseUrl: String,
            val path: String,
            val method: MiddlewareHttpMethods,
            val queries: Map<String, String>,
            val headers: Map<String, String>,
            val body: String?
        )

        override suspend fun testOriginalRoute(
            originalBaseUrl: String,
            originalPath: String,
            originalMethod: MiddlewareHttpMethods,
            originalQueries: Map<String, String>,
            originalHeaders: Map<String, String>,
            originalBody: String?
        ): Pair<Int, String?> {
            lastTestRequest = TestRequestParams(
                originalBaseUrl,
                originalPath,
                originalMethod,
                originalQueries,
                originalHeaders,
                originalBody
            )
            exceptionToThrow?.let { throw it }
            return responseToReturn
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
        override suspend fun requestPreview(data: PreviewRequest): CommonFlow<Resource<String>> {
            return flowOf(Resource.Success("")).toCommonFlow()
        }
        override suspend fun createNewRoute(data: MappingRequest): CommonFlow<Resource<String>> {
            return flowOf(Resource.Success("")).toCommonFlow()
        }
    }

    // Test data
    private val validBaseUrl = "https://api.example.com"
    private val validPath = "/endpoint"
    private val validMethod = MiddlewareHttpMethods.Post
    private val validQueries = mapOf("param" to "value")
    private val validHeaders = mapOf("Authorization" to "Bearer token")
    private val validBody = "request-body"

    @Test
    fun `invoke should throw when baseUrl is blank`() = runTest {
        // Given
        val useCase = TestOriginalRouteUseCase(FakeMiddlewareRepository())

        // When / Then
        assertFailsWith<MiddlewareException> {
            useCase(
                originalBaseUrl = "",
                originalPath = validPath,
                originalMethod = validMethod,
                originalQueries = validQueries,
                originalHeaders = validHeaders,
                originalBody = validBody
            )
        }.let { exception ->
            assertEquals("Original base url is empty", exception.message)
        }
    }

    @Test
    fun `invoke should throw when baseUrl is invalid`() = runTest {
        // Given
        val useCase = TestOriginalRouteUseCase(FakeMiddlewareRepository())

        // When / Then
        assertFailsWith<MiddlewareException> {
            useCase(
                originalBaseUrl = "invalid-url",
                originalPath = validPath,
                originalMethod = validMethod,
                originalQueries = validQueries,
                originalHeaders = validHeaders,
                originalBody = validBody
            )
        }.let { exception ->
            assertEquals("Original base url is not a valid url", exception.message)
        }
    }

    @Test
    fun `invoke should throw when path is blank`() = runTest {
        // Given
        val useCase = TestOriginalRouteUseCase(FakeMiddlewareRepository())

        // When / Then
        assertFailsWith<MiddlewareException> {
            useCase(
                originalBaseUrl = validBaseUrl,
                originalPath = "",
                originalMethod = validMethod,
                originalQueries = validQueries,
                originalHeaders = validHeaders,
                originalBody = validBody
            )
        }.let { exception ->
            assertEquals("Original path is empty", exception.message)
        }
    }

    @Test
    fun `invoke should call repository with correct parameters`() = runTest {
        // Given
        val fakeRepository = FakeMiddlewareRepository()
        val useCase = TestOriginalRouteUseCase(fakeRepository)

        // When
        useCase(
            originalBaseUrl = validBaseUrl,
            originalPath = validPath,
            originalMethod = validMethod,
            originalQueries = validQueries,
            originalHeaders = validHeaders,
            originalBody = validBody
        )

        // Then
        val request = fakeRepository.lastTestRequest
        assertEquals(validBaseUrl, request?.baseUrl)
        assertEquals(validPath, request?.path)
        assertEquals(validMethod, request?.method)
        assertEquals(validQueries, request?.queries)
        assertEquals(validHeaders, request?.headers)
        assertEquals(validBody, request?.body)
    }

    @Test
    fun `invoke should return response when repository succeeds`() = runTest {
        // Given
        val expectedResponse = Pair(200, "success")
        val fakeRepository = FakeMiddlewareRepository().apply {
            responseToReturn = expectedResponse
        }
        val useCase = TestOriginalRouteUseCase(fakeRepository)

        // When
        val result = useCase(
            originalBaseUrl = validBaseUrl,
            originalPath = validPath,
            originalMethod = validMethod,
            originalQueries = validQueries,
            originalHeaders = validHeaders,
            originalBody = validBody
        )

        // Then
        assertEquals(expectedResponse, result)
    }

    @Test
    fun `invoke should propagate repository exceptions`() = runTest {
        // Given
        val expectedException = RuntimeException("Network error")
        val fakeRepository = FakeMiddlewareRepository().apply {
            exceptionToThrow = expectedException
        }
        val useCase = TestOriginalRouteUseCase(fakeRepository)

        // When / Then
        try {
            useCase(
                originalBaseUrl = validBaseUrl,
                originalPath = validPath,
                originalMethod = validMethod,
                originalQueries = validQueries,
                originalHeaders = validHeaders,
                originalBody = validBody
            )
            assertTrue(false, "Expected exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals(expectedException, e)
        }
    }
}
