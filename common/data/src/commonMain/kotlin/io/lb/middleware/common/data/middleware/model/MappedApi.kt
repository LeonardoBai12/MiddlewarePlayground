package io.lb.middleware.common.data.middleware.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing a mapped API.
 *
 * @property uuid The UUID of the mapped API.
 * @property originalApi The original API.
 */
@Serializable
data class MappedApi(
    @SerialName("uuid")
    val uuid: String,
    @SerialName("originalApi")
    val originalApi: OriginalApi,
)
