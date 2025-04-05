package io.middleware.user.domain.usecases

import io.lb.middleware.common.shared.user.UserException
import io.lb.middleware.common.shared.util.isStrongPassword
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource
import io.middleware.user.domain.repository.UserRepository

class UpdatePasswordUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(
        password: String,
        repeatedPassword: String,
        newPassword: String,
    ) : CommonFlow<Resource<Unit>> {
        if (password.isBlank()) {
            throw UserException("Password cannot be blank")
        }

        if (password != repeatedPassword) {
            throw UserException("Passwords do not match")
        }

        if (newPassword.isStrongPassword().not()) {
            throw UserException("Password is not strong enough")
        }

        return repository.updatePassword(
            password = password,
            newPassword = newPassword
        )
    }
}
