package io.lb.middleware.impl.client.user.remote.model

import io.lb.middleware.common.remote.user.remote.model.LoginResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing a request to login.
 *
 * @property userId The user's UUID.
 * @property token The user's login token.
 */
@Serializable
internal data class LoginResponse(
    @SerialName("userId")
    val userId: String,
    @SerialName("token")
    val token: String,
) {
    fun toResult(): LoginResult = LoginResult(
        userId = userId,
        token = token
    )
}
