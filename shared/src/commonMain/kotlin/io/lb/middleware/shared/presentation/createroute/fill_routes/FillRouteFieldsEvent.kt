package io.lb.middleware.shared.presentation.createroute.fill_routes

import io.lb.middleware.common.shared.middleware.model.NewBodyField
import io.lb.middleware.common.shared.middleware.model.OldBodyField

sealed class FillRouteFieldsEvent {
    data class UpsertNewBodyField(val key: String, val field: NewBodyField) : FillRouteFieldsEvent()
    data class RemoveNewBodyField(val key: String) : FillRouteFieldsEvent()
    data class UpsertOldBodyField(val key: String, val field: OldBodyField) : FillRouteFieldsEvent()
    data class RemoveOldBodyField(val key: String) : FillRouteFieldsEvent()
    data class UpsertPreConfiguredQuery(val key: String, val value: String) : FillRouteFieldsEvent()
    data class RemovePreConfiguredQuery(val key: String) : FillRouteFieldsEvent()
    data class UpsertPreConfiguredHeader(val key: String, val value: String) : FillRouteFieldsEvent()
    data class RemovePreConfiguredHeader(val key: String) : FillRouteFieldsEvent()
}
