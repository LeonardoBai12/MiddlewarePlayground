package io.lb.middleware.shared.presentation.user

sealed class UserEvent {
    data object GetCurrentUser : UserEvent()
    data object ResetUserState : UserEvent()
    data class UpdateUser(
        val userName: String,
        val phone: String,
        val email: String,
        val profilePictureUrl: String?,
        val password: String,
    ) : UserEvent()
    data class UpdatePassword(
        val password: String,
        val repeatedPassword: String,
        val newPassword: String,
    ) : UserEvent()
    data class DeleteUser(val password: String) : UserEvent()
    data object Logout : UserEvent()
}
