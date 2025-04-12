package io.lb.middleware.common.remote.user.remote.model

data class LoginResult(
    val token: String,
    val userId: String,
)
