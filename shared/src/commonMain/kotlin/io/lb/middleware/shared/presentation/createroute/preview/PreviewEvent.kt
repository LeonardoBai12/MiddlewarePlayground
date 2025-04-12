package io.lb.middleware.shared.presentation.createroute.preview

import io.lb.middleware.common.shared.middleware.model.NewBodyField
import io.lb.middleware.common.shared.middleware.model.OldBodyField

sealed class PreviewEvent {
    data class RequestPreview(
        val response: String,
        val newBodyFields: Map<String, NewBodyField>,
        val oldBodyFields: Map<String, OldBodyField>,
        val ignoreEmptyValues: Boolean
    ) : PreviewEvent()
}
