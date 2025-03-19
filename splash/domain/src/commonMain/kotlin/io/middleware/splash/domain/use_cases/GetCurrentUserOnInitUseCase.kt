package io.middleware.splash.domain.use_cases

import io.middleware.splash.domain.repository.SplashRepository

class GetCurrentUserOnInitUseCase(
    private val repository: SplashRepository
) {
    suspend operator fun invoke() = repository.getCurrentUser()
}
