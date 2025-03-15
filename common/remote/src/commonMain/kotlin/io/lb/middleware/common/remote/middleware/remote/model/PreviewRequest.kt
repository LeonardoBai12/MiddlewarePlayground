package io.lb.middleware.common.data.middleware.remote.model

import io.lb.middleware.common.shared.middleware.model.MappingRules

data class PreviewRequest(
    val originalResponse: String,
    val mappingRules: MappingRules
)
