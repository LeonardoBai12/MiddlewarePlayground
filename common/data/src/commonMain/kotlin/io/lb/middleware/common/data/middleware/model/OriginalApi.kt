package io.lb.middleware.common.data.middleware.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing an original API.
 *
 * @property baseUrl The base URL of the original API.
 */
@Serializable
data class OriginalApi(
    @SerialName("baseUrl")
    val baseUrl: String
)
