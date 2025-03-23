package io.lb.middleware.android.routes.details.model

import android.os.Parcelable
import io.lb.middleware.common.shared.middleware.model.MappedRoute
import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods
import kotlinx.parcelize.Parcelize

@Parcelize
data class AndroidMappedRoute(
    val uuid: String,
    val path: String,
    val method: MiddlewareHttpMethods,
    val originalBaseUrl: String,
    val originalPath: String,
    val originalMethod: MiddlewareHttpMethods,
    val originalQueries: Map<String, String> = mapOf(),
    val originalHeaders: Map<String, String> = mapOf(),
    val originalBody: String?,
    val preConfiguredQueries: Map<String, String> = mapOf(),
    val preConfiguredHeaders: Map<String, String> = mapOf(),
    val preConfiguredBody: String? = null,
    val isFavourite: Boolean
) : Parcelable {
    fun toMappedRoute() = MappedRoute(
        uuid = uuid,
        path = path,
        method = method,
        originalBaseUrl = originalBaseUrl,
        originalPath = originalPath,
        originalMethod = originalMethod,
        originalQueries = originalQueries,
        originalHeaders = originalHeaders,
        originalBody = originalBody,
        preConfiguredQueries = preConfiguredQueries,
        preConfiguredHeaders = preConfiguredHeaders,
        preConfiguredBody = preConfiguredBody,
        isFavourite = isFavourite
    )

    companion object {
        fun fromMappedRoute(mappedRoute: MappedRoute) = AndroidMappedRoute(
            uuid = mappedRoute.uuid,
            path = mappedRoute.path,
            method = mappedRoute.method,
            originalBaseUrl = mappedRoute.originalBaseUrl,
            originalPath = mappedRoute.originalPath,
            originalMethod = mappedRoute.originalMethod,
            originalQueries = mappedRoute.originalQueries,
            originalHeaders = mappedRoute.originalHeaders,
            originalBody = mappedRoute.originalBody,
            preConfiguredQueries = mappedRoute.preConfiguredQueries,
            preConfiguredHeaders = mappedRoute.preConfiguredHeaders,
            preConfiguredBody = mappedRoute.preConfiguredBody,
            isFavourite = mappedRoute.isFavourite
        )
    }
}
