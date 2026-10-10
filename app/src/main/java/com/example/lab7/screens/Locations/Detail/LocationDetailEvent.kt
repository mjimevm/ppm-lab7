package com.example.lab7.screens.Locations.Detail

sealed interface LocationDetailEvent {
    data object LoadLocation : LocationDetailEvent
    data object SetError : LocationDetailEvent
}
