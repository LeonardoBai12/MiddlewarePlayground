package io.lb.middleware.common.remote.user.remote.model

/**
 * Data class representing a request to login.
 *
 * @property password The user's password.
 * @property email The user's email address.
 */
data class LoginRequest(
    val email: String,
    val password: String
)
