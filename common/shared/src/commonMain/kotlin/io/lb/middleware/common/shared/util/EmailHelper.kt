package io.lb.middleware.common.shared.util

fun String.isValidEmail(): Boolean {
    val emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    return this.matches(emailRegex.toRegex())
}
