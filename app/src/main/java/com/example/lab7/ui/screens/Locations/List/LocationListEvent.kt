package com.example.lab7.ui.screens.Locations.List

enum class LocationFilter {
    ALL, FAVORITES, NON_FAVORITES
}

sealed interface LocationListEvent {
    data object LoadLocations : LocationListEvent
    data object SetError : LocationListEvent
    data class ToggleFavorite(val locationId: Int, val currentFavorite: Boolean) : LocationListEvent
    data class SetFilter(val filter: LocationFilter) : LocationListEvent
}
