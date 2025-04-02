package io.lb.middleware.impl.client.middleware.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.head
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.lb.middleware.common.remote.middleware.remote.MiddlewareClientService
import io.lb.middleware.common.remote.middleware.remote.model.MappedRouteResult
import io.lb.middleware.common.shared.middleware.error.MiddlewareException
import io.lb.middleware.common.shared.middleware.model.MappedRoute
import io.lb.middleware.common.shared.middleware.model.MappingRequest
import io.lb.middleware.common.shared.middleware.model.PreviewRequest
import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods
import io.lb.middleware.impl.client.NetworkConstants
import io.lb.middleware.impl.client.middleware.remote.model.MappedRouteParameter
import io.lb.middleware.impl.client.middleware.remote.model.MappedRouteResponse
import io.lb.middleware.impl.client.middleware.remote.model.NewBodyFieldParameter
import io.lb.middleware.impl.client.middleware.remote.model.NewBodyMappingRuleParameter
import io.lb.middleware.impl.client.middleware.remote.model.OldBodyFieldParameter
import io.lb.middleware.impl.client.middleware.remote.model.OriginalApiParameter
import io.lb.middleware.impl.client.middleware.remote.model.OriginalRouteParameter
import io.lb.middleware.impl.client.middleware.remote.model.PreviewParameter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

/**
 * Service for making requests to the middleware server.
 */
class MiddlewareClientServiceImpl(
    private val httpClient: HttpClient
) : MiddlewareClientService {
    private val baseUrl = NetworkConstants.MIDDLEWARE_BASE_URL
    private val json = Json {
        ignoreUnknownKeys = true
    }

    override suspend fun requestMappedRoute(
        token: String,
        path: String,
        method: MiddlewareHttpMethods,
        queries: Map<String, String>,
        preConfiguredQueries: Map<String, String>,
        preConfiguredHeaders: Map<String, String>,
        preConfiguredBody: String?,
    ): String? {
        return runCatching {
            when (method) {
                MiddlewareHttpMethods.Delete -> {
                    httpClient.delete {
                        genericRequest(
                            path = path,
                            token = token,
                            preConfiguredQueries = preConfiguredQueries + queries,
                            preConfiguredHeaders = preConfiguredHeaders,
                            preConfiguredBody = preConfiguredBody
                        )
                    }
                }

                MiddlewareHttpMethods.Get -> {
                    val result = httpClient.get {
                        genericRequest(
                            path = path,
                            token = token,
                            preConfiguredQueries = preConfiguredQueries + queries,
                            preConfiguredHeaders = preConfiguredHeaders,
                            preConfiguredBody = preConfiguredBody
                        )
                    }
                    result
                }

                MiddlewareHttpMethods.Head -> {
                    httpClient.head {
                        genericRequest(
                            path = path,
                            token = token,
                            preConfiguredQueries = preConfiguredQueries + queries,
                            preConfiguredHeaders = preConfiguredHeaders,
                            preConfiguredBody = preConfiguredBody
                        )
                    }
                }

                MiddlewareHttpMethods.Patch -> {
                    httpClient.patch {
                        genericRequest(
                            path = path,
                            token = token,
                            preConfiguredQueries = preConfiguredQueries + queries,
                            preConfiguredHeaders = preConfiguredHeaders,
                            preConfiguredBody = preConfiguredBody
                        )
                    }
                }

                MiddlewareHttpMethods.Post -> {
                    httpClient.post {
                        genericRequest(
                            path = path,
                            token = token,
                            preConfiguredQueries = preConfiguredQueries + queries,
                            preConfiguredHeaders = preConfiguredHeaders,
                            preConfiguredBody = preConfiguredBody
                        )
                    }
                }

                MiddlewareHttpMethods.Put -> {
                    httpClient.put {
                        genericRequest(
                            path = path,
                            token = token,
                            preConfiguredQueries = preConfiguredQueries + queries,
                            preConfiguredHeaders = preConfiguredHeaders,
                            preConfiguredBody = preConfiguredBody
                        )
                    }
                }
            }.bodyAsText()
        }.getOrNull()
    }

    private fun HttpRequestBuilder.genericRequest(
        requestBaseUrl: String = baseUrl,
        path: String,
        token: String? = null,
        preConfiguredQueries: Map<String, String>,
        preConfiguredHeaders: Map<String, String>,
        preConfiguredBody: String?
    ) {
        url("$requestBaseUrl${path}")
        contentType(ContentType.Application.Json)
        token?.let { bearerAuth(it) }
        preConfiguredQueries.forEach { (key, value) ->
            parameter(key, value)
        }
        preConfiguredHeaders.forEach { (key, value) ->
            headers.append(key, value)
        }
        preConfiguredBody?.takeIf {
            it.isNotBlank() && it != "null" && it != "{}"
        }?.let {
            setBody(json.encodeToString(it))
        }
    }

    override suspend fun testOriginalRoute(
        originalBaseUrl: String,
        originalPath: String,
        originalMethod: MiddlewareHttpMethods,
        originalQueries: Map<String, String>,
        originalHeaders: Map<String, String>,
        originalBody: String?
    ): Pair<Int, String?> {
        val result = when (originalMethod) {
            MiddlewareHttpMethods.Delete -> {
                httpClient.delete {
                    genericRequest(
                        requestBaseUrl = originalBaseUrl,
                        path = originalPath,
                        preConfiguredQueries = originalQueries,
                        preConfiguredHeaders = originalHeaders,
                        preConfiguredBody = originalBody
                    )
                }
            }

            MiddlewareHttpMethods.Get -> {
                httpClient.get {
                    genericRequest(
                        requestBaseUrl = originalBaseUrl,
                        path = originalPath,
                        preConfiguredQueries = originalQueries,
                        preConfiguredHeaders = originalHeaders,
                        preConfiguredBody = originalBody
                    )
                }
            }

            MiddlewareHttpMethods.Head -> {
                httpClient.head {
                    genericRequest(
                        requestBaseUrl = originalBaseUrl,
                        path = originalPath,
                        preConfiguredQueries = originalQueries,
                        preConfiguredHeaders = originalHeaders,
                        preConfiguredBody = originalBody
                    )
                }
            }

            MiddlewareHttpMethods.Patch -> {
                httpClient.patch {
                    genericRequest(
                        requestBaseUrl = originalBaseUrl,
                        path = originalPath,
                        preConfiguredQueries = originalQueries,
                        preConfiguredHeaders = originalHeaders,
                        preConfiguredBody = originalBody
                    )
                }
            }

            MiddlewareHttpMethods.Post -> {
                httpClient.post {
                    genericRequest(
                        requestBaseUrl = originalBaseUrl,
                        path = originalPath,
                        preConfiguredQueries = originalQueries,
                        preConfiguredHeaders = originalHeaders,
                        preConfiguredBody = originalBody
                    )
                }
            }

            MiddlewareHttpMethods.Put -> {
                httpClient.put {
                    genericRequest(
                        requestBaseUrl = originalBaseUrl,
                        path = originalPath,
                        preConfiguredQueries = originalQueries,
                        preConfiguredHeaders = originalHeaders,
                        preConfiguredBody = originalBody
                    )
                }
            }
        }
        val statusCode = result.status.value
        val body = kotlin.runCatching { result.bodyAsText() }.getOrNull()
        return Pair(statusCode, body)
    }

    override suspend fun requestPreview(token: String, data: PreviewRequest): String {
        val result = httpClient.post {
            url("$baseUrl/v1/preview")
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(
                PreviewParameter(
                    originalResponse = Json.parseToJsonElement(data.originalResponse),
                    mappingRules = NewBodyMappingRuleParameter(
                        newBodyFields = data.mappingRules.newBodyFields.mapValues {
                            NewBodyFieldParameter.fromNewBodyField(it.value)
                        },
                        oldBodyFields = data.mappingRules.oldBodyFields.mapValues {
                            OldBodyFieldParameter.fromOldBodyField(it.value)
                        },
                        ignoreEmptyValues = data.mappingRules.ignoreEmptyValues
                    )
                )
            )
        }

        if (result.status != HttpStatusCode.OK) {
            throw MiddlewareException(result.bodyAsText())
        }

        return result.bodyAsText()
    }

    override suspend fun createNewRoute(token: String, data: MappingRequest): String {
        val result = httpClient.post {
            url("$baseUrl/v1/mapping")
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(
                MappedRouteParameter(
                    path = data.path,
                    originalRoute = OriginalRouteParameter(
                        path = data.originalPath,
                        originalApi = OriginalApiParameter(
                            baseUrl = data.originalBaseUrl
                        ),
                        method = data.originalMethod,
                        queries = data.originalQueries,
                        headers = data.originalHeaders,
                        body = data.originalBody.let {
                            json.parseToJsonElement(it).jsonObject
                        }
                    ),
                    method = data.method,
                    preConfiguredQueries = data.preConfiguredQueries,
                    preConfiguredHeaders = data.preConfiguredHeaders,
                    preConfiguredBody = data.preConfiguredBody?.let {
                        json.parseToJsonElement(it).jsonObject
                    } ?: data.originalBody.let {
                        json.parseToJsonElement(it).jsonObject
                    },
                    mappingRules = NewBodyMappingRuleParameter(
                        newBodyFields = data.mappingRules.newBodyFields.mapValues {
                            NewBodyFieldParameter.fromNewBodyField(it.value)
                        },
                        oldBodyFields = data.mappingRules.oldBodyFields.mapValues {
                            OldBodyFieldParameter.fromOldBodyField(it.value)
                        },
                        ignoreEmptyValues = data.mappingRules.ignoreEmptyValues
                    )
                )
            )
        }

        if (result.status != HttpStatusCode.OK) {
            throw MiddlewareException(result.bodyAsText())
        }

        return result.bodyAsText()
    }

    override suspend fun getAllRoutes(token: String): List<MappedRouteResult> {
        val result = httpClient.get {
            url("$baseUrl/v1/routes")
            contentType(ContentType.Application.Json)
            bearerAuth(token)
        }

        if (result.status != HttpStatusCode.OK) {
            throw MiddlewareException(result.bodyAsText())
        }

        return result.body<List<MappedRouteResponse>?>()?.map {
            MappedRouteResult(
                uuid = it.uuid,
                path = it.path,
                method = it.method,
                originalBaseUrl = it.originalRoute.originalApi.baseUrl,
                originalPath = it.originalRoute.path,
                originalMethod = it.originalRoute.method,
                originalQueries = it.originalRoute.queries,
                originalHeaders = it.originalRoute.headers,
                originalBody = it.originalRoute.body?.toString(),
                preConfiguredQueries = it.preConfiguredQueries,
                preConfiguredHeaders = it.preConfiguredHeaders,
                preConfiguredBody = json.encodeToString(it.preConfiguredBody),
            )
        } ?: emptyList()
    }
}
