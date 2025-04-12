package io.lb.middleware.shared.presentation.splash

sealed class SplashEvent {
    data object GetCurrentUser : SplashEvent()
}
