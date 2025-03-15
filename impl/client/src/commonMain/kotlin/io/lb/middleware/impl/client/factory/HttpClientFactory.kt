package io.lb.middleware.impl.client.factory

import io.ktor.client.HttpClient

internal expect class HttpClientFactory {
    fun create(): HttpClient
}
