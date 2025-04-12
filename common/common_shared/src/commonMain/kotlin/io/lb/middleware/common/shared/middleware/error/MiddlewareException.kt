package io.lb.middleware.common.shared.middleware.error

class MiddlewareException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)
