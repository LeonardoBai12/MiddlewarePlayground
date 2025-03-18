package io.middleware.user.domain.use_cases

import io.middleware.user.domain.repository.UserRepository

class GetCurrentUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke() = repository.getCurrentUser()
}
