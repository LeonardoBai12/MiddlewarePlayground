package io.middleware.signup.domain.usecases

import io.lb.middleware.common.shared.user.UserData
import io.lb.middleware.common.shared.util.isStrongPassword
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource
import io.middleware.signup.domain.error.SignUpException
import io.middleware.signup.domain.repository.SignUpRepository
import io.lb.middleware.common.shared.util.isValidEmail
import io.lb.middleware.common.shared.util.isValidPhone
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

        if (email.isValidEmail().not()) {
            throw SignUpException("Invalid email")
        }

        if (password.isBlank()) {
            throw SignUpException("Password cannot be blank")
        }

        if (password.isStrongPassword().not()) {
            throw SignUpException("Password is not strong enough")
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

        if (phone.isValidPhone().not()) {
            throw SignUpException("Invalid phone")
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
