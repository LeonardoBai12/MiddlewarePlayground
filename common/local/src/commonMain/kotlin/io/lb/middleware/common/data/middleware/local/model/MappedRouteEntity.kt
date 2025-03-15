package io.lb.middleware.common.data.middleware.local.model

data class MappedRouteEntity(
    val id: String,
    val path: String,
    val method: String,
    val originalApiId: String,
    val originalApiPath: String,
    val originalApiMethod: String,
    val originalApiBaseUrl: String
)
