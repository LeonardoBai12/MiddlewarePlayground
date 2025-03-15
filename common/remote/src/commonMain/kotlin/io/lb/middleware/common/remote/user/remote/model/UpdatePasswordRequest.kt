package io.lb.middleware.common.data.user.remote.model

/**
 * Data class representing a request to update a user's password.
 *
 * @property password The current password.
 * @property newPassword The new password.
 */
data class UpdatePasswordRequest(
    val password: String,
    val newPassword: String,
)
