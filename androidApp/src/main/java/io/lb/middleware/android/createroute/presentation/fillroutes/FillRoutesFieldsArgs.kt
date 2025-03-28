package io.lb.middleware.android.createroute.presentation.fillroutes

import io.lb.middleware.common.shared.middleware.model.OldBodyField
import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods

data class FillRoutesFieldsArgs(
    val originalBaseUrl: String,
    val originalPath: String,
    val originalMethod: MiddlewareHttpMethods,
    val originalBody: String,
    val originalQueries: Map<String, String>,
    val oldBodyFields: Map<String, OldBodyField>,
)
