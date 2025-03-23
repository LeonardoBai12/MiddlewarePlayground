package io.middleware.sign_up.domain.use_cases

import io.lb.middleware.common.shared.user.UserData
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource
import io.middleware.sign_up.domain.error.SignUpException
import io.middleware.sign_up.domain.repository.SignUpRepository
import io.lb.middleware.common.shared.util.isValidEmail

class LoginUseCase(
    private val repository: SignUpRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
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

        return repository.login(
            email = email,
            password = password
        )
    }
}
