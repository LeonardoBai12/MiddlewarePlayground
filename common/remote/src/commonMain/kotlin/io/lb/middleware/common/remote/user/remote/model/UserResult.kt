package io.lb.middleware.common.remote.user.remote.model

data class UserResult(
    val userId: String,
    val userName: String,
    val phone: String,
    val password: String? = null,
    val email: String,
    val profilePictureUrl: String? = null
)
