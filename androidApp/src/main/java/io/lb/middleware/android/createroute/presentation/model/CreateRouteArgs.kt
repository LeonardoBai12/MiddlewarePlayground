package io.lb.middleware.android.createroute.presentation.model

import android.os.Parcelable
import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateRouteArgs(
    val originalBaseUrl: String,
    val originalPath: String,
    val originalMethod: MiddlewareHttpMethods = MiddlewareHttpMethods.Get,
    val originalBody: String = "",
    val originalQueries: Map<String, String> = mapOf(),
    val originalHeaders: Map<String, String> = mapOf(),
    val oldBodyFields: Map<String, AndroidOldBodyField> = mapOf(),
    val newBodyFields: Map<String, AndroidNewBodyField> = mapOf(),
    val mappedPath: String = originalPath,
    val mappedMethod: MiddlewareHttpMethods = originalMethod,
    val preConfiguredBody: String = "",
    val preConfiguredQueries: Map<String, String> = mapOf(),
    val preConfiguredHeaders: Map<String, String> = mapOf(),
) : Parcelable
