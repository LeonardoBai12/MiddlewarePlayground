package io.lb.middleware.shared.presentation.createroute.fillpreconfigs

sealed class FillPreConfigsEvent {
    data class UpsertPreConfiguredQuery(val key: String, val value: String) : FillPreConfigsEvent()
    data class RemovePreConfiguredQuery(val key: String) : FillPreConfigsEvent()
    data class UpsertPreConfiguredHeader(val key: String, val value: String) : FillPreConfigsEvent()
    data class RemovePreConfiguredHeader(val key: String) : FillPreConfigsEvent()
}
