package io.lb.middleware.impl.client.user.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing a request to create a new user.
 *
 * @property userName The username.
 * @property password The user's password.
 * @property email The user's email address (optional).
 * @property profilePictureUrl The URL of the user's profile picture (optional).
 */
@Serializable
internal data class UserUpdateParameter(
    @SerialName("userName")
    val userName: String? = null,
    @SerialName("password")
    val password: String,
    @SerialName("email")
    val email: String? = null,
    @SerialName("profilePictureUrl")
    val profilePictureUrl: String? = null
)
