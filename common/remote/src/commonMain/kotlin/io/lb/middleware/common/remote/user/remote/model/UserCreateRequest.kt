package io.lb.middleware.common.data.user.remote.model

/**
 * Data class representing a request to create a new user.
 *
 * @property userName The username.
 * @property phone User's phone number
 * @property password The user's password.
 * @property email The user's email address.
 * @property profilePictureUrl The URL of the user's profile picture (optional).
 */
data class UserCreateRequest(
    val userName: String,
    val phone: String,
    val password: String,
    val email: String,
    val profilePictureUrl: String? = null
)
