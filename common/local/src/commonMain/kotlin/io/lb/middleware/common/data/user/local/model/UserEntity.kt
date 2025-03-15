package io.lb.middleware.common.data.user.local.model

data class UserEntity(
    val token: String,
    val userId: String,
    val userName: String,
    val phone: String,
    val password: String? = null,
    val email: String,
    val profilePictureUrl: String? = null
)
