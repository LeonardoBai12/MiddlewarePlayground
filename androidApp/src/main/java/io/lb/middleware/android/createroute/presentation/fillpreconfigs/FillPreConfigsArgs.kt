package io.lb.middleware.android.createroute.presentation.fillpreconfigs

import android.os.Parcelable
import io.lb.middleware.android.createroute.presentation.model.AndroidNewBodyField
import io.lb.middleware.android.createroute.presentation.model.AndroidOldBodyField
import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods
import kotlinx.parcelize.Parcelize

@Parcelize
data class FillPreConfigsArgs(
    val originalBaseUrl: String,
    val originalPath: String,
    val originalMethod: MiddlewareHttpMethods,
    val originalBody: String,
    val originalQueries: Map<String, String>,
    val oldBodyFields: Map<String, AndroidOldBodyField>,
    val newBodyField: Map<String, AndroidNewBodyField>
) : Parcelable
