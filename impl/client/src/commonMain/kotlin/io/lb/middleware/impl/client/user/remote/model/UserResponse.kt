package io.lb.middleware.impl.client.user.remote.model

import io.lb.middleware.common.remote.user.remote.model.UserResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing user data.
 *
 * @property userId The unique identifier for the user.
 * @property userName The username.
 * @property password The user's password (returns null on selects).
 * @property email The user's email address.
 * @property profilePictureUrl The URL of the user's profile picture (optional).
 */
@Serializable
internal data class UserResponse(
    @SerialName("userId")
    val userId: String,
    @SerialName("userName")
    val userName: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("password")
    val password: String? = null,
    @SerialName("email")
    val email: String,
    @SerialName("profilePictureUrl")
    val profilePictureUrl: String? = null
) {
    fun toResult(): UserResult {
        return UserResult(
            userId = userId,
            userName = userName,
            phone = phone,
            email = email,
            profilePictureUrl = profilePictureUrl
        )
    }
}
