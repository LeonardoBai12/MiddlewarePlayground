package io.middleware.splash.domain.usecases

import io.middleware.splash.domain.repository.SplashRepository

class GetCurrentUserOnInitUseCase(
    private val repository: SplashRepository
) {
    suspend operator fun invoke() = repository.getCurrentUser()
}
