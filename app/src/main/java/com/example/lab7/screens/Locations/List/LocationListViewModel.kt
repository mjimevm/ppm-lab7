package com.example.lab7.screens.Locations.List

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab7.screens.Locations.Classes.LocationDb
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LocationListUiState())
    val uiState: StateFlow<LocationListUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        onEvent(LocationListEvent.LoadLocations)
    }

    fun onEvent(event: LocationListEvent) {
        when (event) {
            is LocationListEvent.LoadLocations -> loadLocations()
            is LocationListEvent.SetError -> setError()
        }
    }

    fun loadLocations() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, hasError = false) }
            delay(4000)
            try {
                val locations = LocationDb().getAllLocations()
                _uiState.update { it.copy(isLoading = false, data = locations, hasError = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, hasError = true) }
            }
        }
    }

    fun setError() {
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = false, hasError = true) }
    }
}
