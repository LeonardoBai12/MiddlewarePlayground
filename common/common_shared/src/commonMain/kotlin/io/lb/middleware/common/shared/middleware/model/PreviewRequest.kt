package io.lb.middleware.common.shared.middleware.model

data class PreviewRequest(
    val originalResponse: String,
    val mappingRules: MappingRules
)
