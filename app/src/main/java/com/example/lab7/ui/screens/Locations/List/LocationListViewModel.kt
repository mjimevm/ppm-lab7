package com.example.lab7.ui.screens.Locations.List

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab7.data.local.AppDatabase
import com.example.lab7.data.repository.LocationRepository
import com.example.lab7.data.repository.LocationRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationListViewModel(application: Application) : AndroidViewModel(application) {
    private val locationRepository: LocationRepository = LocationRepositoryImpl(AppDatabase.getDatabase(application).locationDao())

    private val _uiState = MutableStateFlow(LocationListUiState())
    val uiState: StateFlow<LocationListUiState> = _uiState.asStateFlow()

    init {
        loadLocations()
    }

    fun onEvent(event: LocationListEvent) {
        when (event) {
            is LocationListEvent.LoadLocations -> loadLocations()
            is LocationListEvent.SetError -> setError()
            is LocationListEvent.ToggleFavorite -> toggleFavorite(event.locationId, event.currentFavorite)
            is LocationListEvent.SetFilter -> setFilter(event.filter)
        }
    }

    fun loadLocations() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, hasError = false) }
            try {
                locationRepository.locations.collectLatest { locations ->
                    _uiState.update { it.copy(isLoading = false, data = locations, hasError = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, hasError = true) }
            }
        }
    }

    fun setError() {
        _uiState.update { it.copy(isLoading = false, hasError = true) }
    }

    private fun toggleFavorite(locationId: Int, currentFavorite: Boolean) {
        viewModelScope.launch {
            locationRepository.updateFavorite(locationId, !currentFavorite)
        }
    }

    private fun setFilter(filter: LocationFilter) {
        _uiState.update { it.copy(filter = filter) }
    }
}
