package io.middleware.api.domain.usecases

import io.lb.middleware.common.shared.middleware.model.MappedRoute
import io.lb.middleware.common.shared.middleware.model.MappingRequest
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
import kotlin.test.assertTrue

class GetAllRoutesUseCaseTest {

    private class FakeMiddlewareRepository : MiddlewareRepository {
        var shouldSucceed = true
        var throwableToEmit: Throwable? = null
        var routesToReturn = emptyList<MappedRoute>()

        override suspend fun getAllRoutes(): CommonFlow<Resource<List<MappedRoute>>> {
            return if (shouldSucceed) {
                flowOf(Resource.Success(routesToReturn)).toCommonFlow()
            } else {
                val error = throwableToEmit ?: RuntimeException("Failed to get routes")
                flowOf(Resource.Error<List<MappedRoute>>(error)).toCommonFlow()
            }
        }

        override suspend fun requestMappedRoute(
            path: String,
            method: MiddlewareHttpMethods,
            queries: Map<String, String>,
            preConfiguredQueries: Map<String, String>,
            preConfiguredHeaders: Map<String, String>,
            preConfiguredBody: String?
        ): String? = null

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

    @Test
    fun `invoke should return empty list when repository returns empty list`() = runTest {
        // Given
        val fakeRepository = FakeMiddlewareRepository().apply {
            routesToReturn = emptyList()
        }
        val useCase = GetAllRoutesUseCase(fakeRepository)

        // When
        val result = useCase().first()

        // Then
        assertTrue(result is Resource.Success)
        assertTrue(result.data?.isEmpty() == true)
    }

    @Test
    fun `invoke should return routes when repository returns routes`() = runTest {
        // Given
        val expectedRoutes = listOf(
            MappedRoute(
                uuid = "route1",
                path = "/api/test1",
                method = MiddlewareHttpMethods.Get,
                originalBaseUrl = "https://api.example.com",
                originalPath = "/original1",
                originalMethod = MiddlewareHttpMethods.Get,
                originalBody = null,
                isFavourite = false
            ),
            MappedRoute(
                uuid = "route2",
                path = "/api/test2",
                method = MiddlewareHttpMethods.Post,
                originalBaseUrl = "https://api.example.com",
                originalPath = "/original2",
                originalMethod = MiddlewareHttpMethods.Post,
                originalBody = "{}",
                isFavourite = true
            )
        )
        val fakeRepository = FakeMiddlewareRepository().apply {
            routesToReturn = expectedRoutes
        }
        val useCase = GetAllRoutesUseCase(fakeRepository)

        // When
        val result = useCase().first()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(expectedRoutes, (result as Resource.Success).data)
    }

    @Test
    fun `invoke should return error when repository fails`() = runTest {
        // Given
        val expectedError = RuntimeException("Test error")
        val fakeRepository = FakeMiddlewareRepository().apply {
            shouldSucceed = false
            throwableToEmit = expectedError
        }
        val useCase = GetAllRoutesUseCase(fakeRepository)

        // When
        val result = useCase().first()

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(expectedError, (result as Resource.Error).throwable)
    }

    @Test
    fun `invoke should return routes with all fields populated`() = runTest {
        // Given
        val expectedRoute = MappedRoute(
            uuid = "route1",
            path = "/api/test",
            method = MiddlewareHttpMethods.Put,
            originalBaseUrl = "https://api.example.com",
            originalPath = "/original",
            originalMethod = MiddlewareHttpMethods.Post,
            originalQueries = mapOf("param" to "value"),
            originalHeaders = mapOf("Authorization" to "Bearer token"),
            originalBody = "{}",
            preConfiguredQueries = mapOf("fixed" to "value"),
            preConfiguredHeaders = mapOf("Content-Type" to "application/json"),
            preConfiguredBody = "pre-configured",
            isFavourite = true
        )
        val fakeRepository = FakeMiddlewareRepository().apply {
            routesToReturn = listOf(expectedRoute)
        }
        val useCase = GetAllRoutesUseCase(fakeRepository)

        // When
        val result = useCase().first()

        // Then
        assertTrue(result is Resource.Success)
        val route = result.data?.first()
        assertEquals(expectedRoute.uuid, route?.uuid)
        assertEquals(expectedRoute.path, route?.path)
        assertEquals(expectedRoute.method, route?.method)
        assertEquals(expectedRoute.originalBaseUrl, route?.originalBaseUrl)
        assertEquals(expectedRoute.originalPath, route?.originalPath)
        assertEquals(expectedRoute.originalMethod, route?.originalMethod)
        assertEquals(expectedRoute.originalQueries, route?.originalQueries)
        assertEquals(expectedRoute.originalHeaders, route?.originalHeaders)
        assertEquals(expectedRoute.originalBody, route?.originalBody)
        assertEquals(expectedRoute.preConfiguredQueries, route?.preConfiguredQueries)
        assertEquals(expectedRoute.preConfiguredHeaders, route?.preConfiguredHeaders)
        assertEquals(expectedRoute.preConfiguredBody, route?.preConfiguredBody)
        assertEquals(expectedRoute.isFavourite, route?.isFavourite)
    }
}
