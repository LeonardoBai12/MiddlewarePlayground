package io.lb.middleware.impl.client.user.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing a request to login.
 *
 * @property password The user's password.
 * @property email The user's email address.
 */
@Serializable
data class LoginRequest(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String
)
