package io.lb.middleware.shared.presentation.user

import io.lb.middleware.common.shared.user.UserData

data class UserState(
    val userData: UserData? = null,
    val userName: String = "",
    val phone: String = "",
    val email: String = "",
    val password: String = "",
    val newPassword: String = "",
    val repeatedPassword: String = "",
    val isLoading: Boolean = false,
)
