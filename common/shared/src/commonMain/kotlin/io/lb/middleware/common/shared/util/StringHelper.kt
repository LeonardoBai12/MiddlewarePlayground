package io.lb.middleware.common.shared.util

fun String.isValidEmail(): Boolean {
    val emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    return this.matches(emailRegex.toRegex())
}

fun String.isValidPhone(): Boolean {
    val phoneRegex = "^[0-9]{11}$"
    return this.matches(phoneRegex.toRegex())
}

fun String.isStrongPassword(): Boolean {
    val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
    return this.matches(passwordRegex.toRegex())
}
