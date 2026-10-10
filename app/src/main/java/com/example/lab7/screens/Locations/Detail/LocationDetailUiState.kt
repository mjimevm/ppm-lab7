package com.example.lab7.screens.Locations.Detail

import com.example.lab7.screens.Locations.Classes.Location

data class LocationDetailUiState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false
)
