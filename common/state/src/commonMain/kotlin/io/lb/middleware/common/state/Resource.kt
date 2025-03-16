package io.lb.middleware.common.state

sealed class Resource<T>(
    val data: T? = null,
    val throwable: Throwable? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(throwable: Throwable, data: T? = null) : Resource<T>(data, throwable)
    data object Loading : Resource<Nothing>()
    data object Loaded : Resource<Nothing>()
}
