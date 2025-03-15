package io.lb.middleware.common.remote.user.remote.model

/**
 * Data class representing a request to create a new user.
 *
 * @property userName The username.
 * @property password The user's password.
 * @property email The user's email address (optional).
 * @property profilePictureUrl The URL of the user's profile picture (optional).
 */
data class UserUpdateRequest(
    val userId: String,
    val userName: String? = null,
    val password: String,
    val phone: String? = null,
    val email: String? = null,
    val profilePictureUrl: String? = null
)
