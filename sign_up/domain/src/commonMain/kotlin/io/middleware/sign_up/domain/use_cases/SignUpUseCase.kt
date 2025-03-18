package io.middleware.sign_up.domain.use_cases

import io.lb.middleware.common.shared.user.UserData
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource
import io.middleware.sign_up.domain.error.SignUpException
import io.middleware.sign_up.domain.repository.SignUpRepository
import io.lb.middleware.common.shared.util.isValidEmail
import kotlin.uuid.ExperimentalUuidApi

@ExperimentalUuidApi
class SignUpUseCase(
    private val repository: SignUpRepository
) {
    suspend operator fun invoke(
        userName: String,
        phone: String,
        email: String,
        profilePictureUrl: String?,
        password: String,
        repeatedPassword: String
    ): CommonFlow<Resource<UserData?>> {
        if (email.isBlank()) {
            throw SignUpException("Email cannot be blank")
        }

        if (email.isValidEmail()) {
            throw SignUpException("Invalid email")
        }

        if (password.isBlank()) {
            throw SignUpException("Password cannot be blank")
        }

        if (password != repeatedPassword) {
            throw SignUpException("Passwords do not match")
        }

        if (userName.isBlank()) {
            throw SignUpException("Name cannot be blank")
        }

        if (phone.isBlank()) {
            throw SignUpException("Phone cannot be blank")
        }

        val userData = UserData(
            userName = userName,
            phone = phone,
            email = email,
            profilePictureUrl = profilePictureUrl,
        )

        return repository.signUp(
            data = userData,
            password = password
        )
    }
}
