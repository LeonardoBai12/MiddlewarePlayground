package io.middleware.user.domain.usecases

import io.middleware.user.domain.repository.UserRepository

class GetCurrentUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke() = repository.getCurrentUser()
}
