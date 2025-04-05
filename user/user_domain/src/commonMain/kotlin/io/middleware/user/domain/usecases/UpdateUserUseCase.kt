package io.middleware.user.domain.usecases

import io.lb.middleware.common.shared.user.UserData
import io.lb.middleware.common.shared.user.UserException
import io.lb.middleware.common.shared.util.isValidEmail
import io.lb.middleware.common.shared.util.isValidPhone
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource
import io.middleware.user.domain.repository.UserRepository

class UpdateUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        userName: String,
        phone: String,
        email: String,
        profilePictureUrl: String?,
        password: String,
    ) : CommonFlow<Resource<UserData?>> {
        if (userName.isBlank()) {
            throw UserException("User name is required.")
        }

        if (phone.isBlank()) {
            throw UserException("Phone is required.")
        }

        if (email.isBlank()) {
            throw UserException("Email is required.")
        }

        if (email.isValidEmail().not()) {
            throw UserException("Invalid email.")
        }

        if (phone.isValidPhone()) {
            throw UserException("Invalid phone")
        }

        if (password.isBlank()) {
            throw UserException("Password is required.")
        }

        val userData = UserData(
            userName = userName,
            phone = phone,
            email = email,
            profilePictureUrl = profilePictureUrl
        )

        return userRepository.updateUser(
            data = userData,
            password = password,
        )
    }
}
