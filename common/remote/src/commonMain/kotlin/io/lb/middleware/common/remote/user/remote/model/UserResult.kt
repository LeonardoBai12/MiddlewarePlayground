package io.lb.middleware.common.data.user.remote.model

data class UserResult(
    val token: String,
    val userId: String,
    val userName: String,
    val phone: String,
    val password: String? = null,
    val email: String,
    val profilePictureUrl: String? = null
)
