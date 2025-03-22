package io.lb.middleware.shared.presentation.sign_up

sealed class SignUpEvent {
    data class RequestLogin(val email: String, val password: String) : SignUpEvent()
    data class RequestSignUp(
        val userName: String,
        val phone: String,
        val email: String,
        val profilePictureUrl: String?,
        val password: String,
        val repeatedPassword: String
    ) : SignUpEvent()
}
