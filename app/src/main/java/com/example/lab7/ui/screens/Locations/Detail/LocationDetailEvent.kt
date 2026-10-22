package com.example.lab7.ui.screens.Locations.Detail

sealed interface LocationDetailEvent {
    data object LoadLocation : LocationDetailEvent
    data object SetError : LocationDetailEvent
}
