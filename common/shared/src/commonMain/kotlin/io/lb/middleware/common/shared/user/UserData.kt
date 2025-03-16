package io.lb.middleware.common.shared.user

data class UserData(
    val userId: String,
    val userName: String,
    val phone: String,
    val email: String,
    val profilePictureUrl: String? = null,
    val token: String? = null,
)
