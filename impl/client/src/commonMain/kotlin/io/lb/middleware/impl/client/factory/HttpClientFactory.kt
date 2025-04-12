package io.lb.middleware.impl.client.factory

import io.ktor.client.HttpClient

expect class HttpClientFactory {
    fun create(): HttpClient
}
