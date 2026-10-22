package com.example.lab7.ui.screens.Locations.List

sealed interface LocationListEvent {
    data object LoadLocations : LocationListEvent
    data object SetError : LocationListEvent
}
