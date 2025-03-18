package io.middleware.user.domain.use_cases

import io.middleware.user.domain.repository.UserRepository

class LogoutUseCase(
    private val userRepository: UserRepository
) {
    /**
     * Logs out the user.
     */
    suspend operator fun invoke() = userRepository.logout()
}
