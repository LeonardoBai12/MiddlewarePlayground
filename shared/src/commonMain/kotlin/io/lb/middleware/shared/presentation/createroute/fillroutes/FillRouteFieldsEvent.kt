package io.lb.middleware.shared.presentation.createroute.fillroutes

import io.lb.middleware.common.shared.middleware.model.NewBodyField
import io.lb.middleware.common.shared.middleware.model.OldBodyField
import io.lb.middleware.shared.presentation.createroute.fillpreconfigs.FillPreConfigsEvent

sealed class FillRouteFieldsEvent {
    data class UpsertNewBodyField(val key: String, val field: NewBodyField) : FillRouteFieldsEvent()
    data class RemoveNewBodyField(val key: String) : FillRouteFieldsEvent()
    data class UpsertOldBodyField(val key: String, val field: OldBodyField) : FillRouteFieldsEvent()
    data class RemoveOldBodyField(val key: String) : FillRouteFieldsEvent()
}
