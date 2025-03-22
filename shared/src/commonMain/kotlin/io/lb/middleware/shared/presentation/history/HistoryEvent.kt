package io.lb.middleware.shared.presentation.history

sealed class HistoryEvent {
    data object GetApisHistory : HistoryEvent()
    data object GetRoutesHistory : HistoryEvent()
    data class GetRoutesByApi(val apiId: String) : HistoryEvent()
    data class DeleteApiFromHistory(val id: String) : HistoryEvent()
    data class DeleteRouteFromHistory(val id: String) : HistoryEvent()
    data class SwitchRouteToFavourite(val id: String, val isFavourite: Boolean) : HistoryEvent()
    data class SwitchApiToFavourite(val id: String, val isFavourite: Boolean) : HistoryEvent()
    data object ClearHistory : HistoryEvent()
}
