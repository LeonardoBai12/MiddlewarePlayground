package io.lb.middleware.shared.presentation.preview

import io.lb.middleware.common.shared.middleware.model.NewBodyField
import io.lb.middleware.common.shared.middleware.model.OldBodyField

sealed class PreviewEvent {
    data class UpsertNewBodyField(val key: String, val field: NewBodyField) : PreviewEvent()
    data class UpsertOldBodyField(val key: String, val field: OldBodyField) : PreviewEvent()
    data class RemoveNewBodyField(val key: String) : PreviewEvent()
    data class RemoveOldBodyField(val key: String) : PreviewEvent()
    data class RequestPreview(val response: String) : PreviewEvent()
}
