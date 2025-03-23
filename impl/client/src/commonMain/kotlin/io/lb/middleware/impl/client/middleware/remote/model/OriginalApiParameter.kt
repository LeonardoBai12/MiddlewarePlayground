package io.lb.middleware.impl.client.middleware.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing an original API.
 *
 * @property baseUrl The base URL of the original API.
 */
@Serializable
data class OriginalApiParameter(
    @SerialName("baseUrl")
    val baseUrl: String
)
