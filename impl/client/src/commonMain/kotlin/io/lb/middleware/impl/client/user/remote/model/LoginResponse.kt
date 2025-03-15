package io.lb.middleware.impl.client.user.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing a request to login.
 *
 * @property userId The user's UUID.
 * @property token The user's login token.
 */
@Serializable
data class LoginResponse(
    @SerialName("userId")
    val userId: String,
    @SerialName("token")
    val token: String,
)
