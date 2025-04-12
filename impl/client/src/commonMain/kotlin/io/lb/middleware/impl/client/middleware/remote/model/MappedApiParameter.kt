package io.lb.middleware.impl.client.middleware.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MappedApiParameter(
    @SerialName("uuid")
    val uuid: String,
    @SerialName("originalApi")
    val originalApi: OriginalApiParameter,
)
