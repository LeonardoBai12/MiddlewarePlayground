package io.lb.middleware.impl.client.user.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing a request to login.
 *
 * @property password The user's password.
 * @property email The user's email address.
 */
@Serializable
internal data class LoginParameter(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String
)
