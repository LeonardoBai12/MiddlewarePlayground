package io.lb.middleware.common.remote.user.remote.model

/**
 * Data class representing a request to update a user's password.
 *
 * @property userId The user ID.
 * @property password The current password.
 * @property newPassword The new password.
 */
data class UpdatePasswordRequest(
    val userId: String,
    val password: String,
    val newPassword: String,
)
