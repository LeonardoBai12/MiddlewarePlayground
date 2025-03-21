package io.middleware.user.domain.use_cases

import io.lb.middleware.common.shared.user.UserException
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

        return repository.updatePassword(
            password = password,
            newPassword = newPassword
        )
    }
}
