package io.lb.middleware.impl.client.middleware.remote

import io.lb.middleware.common.remote.middleware.remote.model.MappingRequest
import io.lb.middleware.common.data.middleware.remote.model.PreviewRequest
import io.lb.middleware.common.remote.middleware.remote.MiddlewareClientService

/**
 * Service for making requests to the middleware server.
 */
class MiddlewareClientServiceImpl : MiddlewareClientService {
    override suspend fun requestPreview(token: String, data: PreviewRequest): String {
        TODO("Not yet implemented")
    }

    override suspend fun createNewRoute(token: String, data: MappingRequest): String {
        TODO("Not yet implemented")
    }

    override suspend fun getAllRoutes(token: String): List<String> {
        TODO("Not yet implemented")
    }
}
