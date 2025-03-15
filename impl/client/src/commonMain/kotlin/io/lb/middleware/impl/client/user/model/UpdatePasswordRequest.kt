package io.lb.middleware.user.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing a request to update a user's password.
 *
 * @property password The current password.
 * @property newPassword The new password.
 */
@Serializable
data class UpdatePasswordRequest(
    @SerialName("password")
    val password: String,
    @SerialName("newPassword")
    val newPassword: String,
)
