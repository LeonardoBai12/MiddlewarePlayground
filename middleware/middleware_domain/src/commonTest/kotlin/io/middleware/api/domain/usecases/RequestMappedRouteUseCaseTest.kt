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
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RequestMappedRouteUseCaseTest {

    private class FakeMiddlewareRepository : MiddlewareRepository {
        var responseToReturn: String? = null
        var exceptionToThrow: Throwable? = null
        var lastRequestPath: String? = null
        var lastRequestMethod: MiddlewareHttpMethods? = null
        var lastRequestQueries: Map<String, String>? = null
        var lastPreConfiguredQueries: Map<String, String>? = null
        var lastPreConfiguredHeaders: Map<String, String>? = null
        var lastPreConfiguredBody: String? = null

        override suspend fun requestMappedRoute(
            path: String,
            method: MiddlewareHttpMethods,
            queries: Map<String, String>,
            preConfiguredQueries: Map<String, String>,
            preConfiguredHeaders: Map<String, String>,
            preConfiguredBody: String?
        ): String? {
            lastRequestPath = path
            lastRequestMethod = method
            lastRequestQueries = queries
            lastPreConfiguredQueries = preConfiguredQueries
            lastPreConfiguredHeaders = preConfiguredHeaders
            lastPreConfiguredBody = preConfiguredBody

            exceptionToThrow?.let { throw it }
            return responseToReturn
        }

        // Other repository methods with empty implementations
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
        override suspend fun requestPreview(data: PreviewRequest): CommonFlow<Resource<String>> {
            return flowOf(Resource.Success("")).toCommonFlow()
        }
        override suspend fun createNewRoute(data: MappingRequest): CommonFlow<Resource<String>> {
            return flowOf(Resource.Success("")).toCommonFlow()
        }
    }

    private val validPath = "/api/test"
    private val validResponse = "response-body"
    private val validMethod = MiddlewareHttpMethods.Post
    private val validQueries = mapOf("param" to "value")
    private val validPreConfiguredQueries = mapOf("fixed" to "value")
    private val validPreConfiguredHeaders = mapOf("Content-Type" to "application/json")
    private val validPreConfiguredBody = "pre-configured-body"

    @Test
    fun `invoke should throw when path is blank`() = runTest {
        // Given
        val useCase = RequestMappedRouteUseCase(FakeMiddlewareRepository())

        // When / Then
        assertFailsWith<MiddlewareException> {
            useCase(
                path = "",
                method = validMethod,
                queries = validQueries,
                preConfiguredQueries = validPreConfiguredQueries,
                preConfiguredHeaders = validPreConfiguredHeaders,
                preConfiguredBody = validPreConfiguredBody
            )
        }.let { exception ->
            assertEquals("Original path is empty", exception.message)
        }
    }

    @Test
    fun `invoke should call repository with correct parameters`() = runTest {
        // Given
        val fakeRepository = FakeMiddlewareRepository()
        val useCase = RequestMappedRouteUseCase(fakeRepository)

        // When
        useCase(
            path = validPath,
            method = validMethod,
            queries = validQueries,
            preConfiguredQueries = validPreConfiguredQueries,
            preConfiguredHeaders = validPreConfiguredHeaders,
            preConfiguredBody = validPreConfiguredBody
        )

        // Then
        assertEquals(validPath, fakeRepository.lastRequestPath)
        assertEquals(validMethod, fakeRepository.lastRequestMethod)
        assertEquals(validQueries, fakeRepository.lastRequestQueries)
        assertEquals(validPreConfiguredQueries, fakeRepository.lastPreConfiguredQueries)
        assertEquals(validPreConfiguredHeaders, fakeRepository.lastPreConfiguredHeaders)
        assertEquals(validPreConfiguredBody, fakeRepository.lastPreConfiguredBody)
    }

    @Test
    fun `invoke should return response when repository returns response`() = runTest {
        // Given
        val fakeRepository = FakeMiddlewareRepository().apply {
            responseToReturn = validResponse
        }
        val useCase = RequestMappedRouteUseCase(fakeRepository)

        // When
        val result = useCase(
            path = validPath,
            method = validMethod,
            queries = validQueries,
            preConfiguredQueries = validPreConfiguredQueries,
            preConfiguredHeaders = validPreConfiguredHeaders,
            preConfiguredBody = validPreConfiguredBody
        )

        // Then
        assertEquals(validResponse, result)
    }

    @Test
    fun `invoke should return null when repository returns null`() = runTest {
        // Given
        val fakeRepository = FakeMiddlewareRepository().apply {
            responseToReturn = null
        }
        val useCase = RequestMappedRouteUseCase(fakeRepository)

        // When
        val result = useCase(
            path = validPath,
            method = validMethod,
            queries = validQueries,
            preConfiguredQueries = validPreConfiguredQueries,
            preConfiguredHeaders = validPreConfiguredHeaders,
            preConfiguredBody = validPreConfiguredBody
        )

        // Then
        assertNull(result)
    }

    @Test
    fun `invoke should propagate repository exceptions`() = runTest {
        // Given
        val expectedException = RuntimeException("Network error")
        val fakeRepository = FakeMiddlewareRepository().apply {
            exceptionToThrow = expectedException
        }
        val useCase = RequestMappedRouteUseCase(fakeRepository)

        // When / Then
        try {
            useCase(
                path = validPath,
                method = validMethod,
                queries = validQueries,
                preConfiguredQueries = validPreConfiguredQueries,
                preConfiguredHeaders = validPreConfiguredHeaders,
                preConfiguredBody = validPreConfiguredBody
            )
            assertTrue(false, "Expected exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals(expectedException, e)
        }
    }

    @Test
    fun `invoke should work with empty optional parameters`() = runTest {
        // Given
        val fakeRepository = FakeMiddlewareRepository()
        val useCase = RequestMappedRouteUseCase(fakeRepository)

        // When
        useCase(
            path = validPath,
            method = validMethod,
            queries = emptyMap(),
            preConfiguredQueries = emptyMap(),
            preConfiguredHeaders = emptyMap(),
            preConfiguredBody = null
        )

        // Then
        assertEquals(emptyMap<String, String>(), fakeRepository.lastRequestQueries)
        assertEquals(emptyMap<String, String>(), fakeRepository.lastPreConfiguredQueries)
        assertEquals(emptyMap<String, String>(), fakeRepository.lastPreConfiguredHeaders)
        assertNull(fakeRepository.lastPreConfiguredBody)
    }
}
