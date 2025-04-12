package io.lb.middleware.impl.client.user.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing a request to update a user's password.
 *
 * @property password The current password.
 * @property newPassword The new password.
 */
@Serializable
internal data class UpdatePasswordParameter(
    @SerialName("password")
    val password: String,
    @SerialName("newPassword")
    val newPassword: String,
)
