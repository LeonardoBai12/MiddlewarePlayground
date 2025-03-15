package io.lb.middleware

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform