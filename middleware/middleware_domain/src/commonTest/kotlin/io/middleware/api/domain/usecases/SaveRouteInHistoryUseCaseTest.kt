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

class SaveRouteInHistoryUseCaseTest {

    private class FakeMiddlewareRepository : MiddlewareRepository {
        var savedRoute: MappedRoute? = null

        override suspend fun saveRouteInHistory(route: MappedRoute) {
            savedRoute = route
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
    fun `invoke should throw when path is blank`() = runTest {
        // Given
        val useCase = SaveRouteInHistoryUseCase(FakeMiddlewareRepository())
        val invalidRoute = MappedRoute(
            uuid = "123",
            path = "",
            method = MiddlewareHttpMethods.Get,
            originalBaseUrl = "https://example.com",
            originalPath = "/path",
            originalMethod = MiddlewareHttpMethods.Get,
            originalBody = null,
            isFavourite = false
        )

        // When / Then
        assertFailsWith<MiddlewareException> {
            useCase(invalidRoute)
        }.let { exception ->
            assertEquals("Path is empty", exception.message)
        }
    }

    @Test
    fun `invoke should save valid route`() = runTest {
        // Given
        val fakeRepository = FakeMiddlewareRepository()
        val useCase = SaveRouteInHistoryUseCase(fakeRepository)
        val expectedRoute = MappedRoute(
            uuid = "123",
            path = "/api/test",
            method = MiddlewareHttpMethods.Post,
            originalBaseUrl = "https://example.com",
            originalPath = "/original",
            originalMethod = MiddlewareHttpMethods.Post,
            originalBody = "{}",
            isFavourite = true
        )

        // When
        useCase(expectedRoute)

        // Then
        assertEquals(expectedRoute, fakeRepository.savedRoute)
    }
}
