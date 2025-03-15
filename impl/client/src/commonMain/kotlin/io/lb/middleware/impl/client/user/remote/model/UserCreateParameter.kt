package io.lb.middleware.impl.client.user.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing a request to create a new user.
 *
 * @property userName The username.
 * @property phone User's phone number
 * @property password The user's password.
 * @property email The user's email address.
 * @property profilePictureUrl The URL of the user's profile picture (optional).
 */
@Serializable
internal data class UserCreateParameter(
    @SerialName("userName")
    val userName: String?,
    @SerialName("phone")
    val phone: String?,
    @SerialName("password")
    val password: String?,
    @SerialName("email")
    val email: String?,
    @SerialName("profilePictureUrl")
    val profilePictureUrl: String? = null
)
