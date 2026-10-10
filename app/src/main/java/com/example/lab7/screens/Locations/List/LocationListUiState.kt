package com.example.lab7.screens.Locations.List

import com.example.lab7.screens.Locations.Classes.Location

data class LocationListUiState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false
)
