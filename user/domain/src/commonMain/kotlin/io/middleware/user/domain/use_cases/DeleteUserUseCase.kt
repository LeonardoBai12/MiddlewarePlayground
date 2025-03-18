package io.middleware.user.domain.use_cases

import io.middleware.user.domain.repository.UserRepository

class DeleteUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(password: String) = repository.deleteUser(password)
}
