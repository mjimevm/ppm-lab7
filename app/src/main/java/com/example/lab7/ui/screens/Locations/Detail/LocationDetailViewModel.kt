package com.example.lab7.ui.screens.Locations.Detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.lab7.core.navigation.LocationDetailDestination
import com.example.lab7.data.local.AppDatabase
import com.example.lab7.data.repository.LocationRepository
import com.example.lab7.data.repository.LocationRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationDetailViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    private val locationId: Int = try {
        savedStateHandle.toRoute<LocationDetailDestination>().locationId
    } catch (e: Exception) {
        1
    }

    private val locationRepository: LocationRepository = LocationRepositoryImpl(AppDatabase.getDatabase(application).locationDao())

    private val _uiState = MutableStateFlow(LocationDetailUiState())
    val uiState: StateFlow<LocationDetailUiState> = _uiState.asStateFlow()

    init {
        loadLocation()
    }

    fun onEvent(event: LocationDetailEvent) {
        when (event) {
            is LocationDetailEvent.LoadLocation -> loadLocation()
            is LocationDetailEvent.SetError -> setError()
            is LocationDetailEvent.ToggleFavorite -> toggleFavorite()
        }
    }

    fun loadLocation() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, hasError = false) }
            try {
                locationRepository.getLocationById(locationId).collectLatest { location ->
                    _uiState.update { it.copy(isLoading = false, data = location, hasError = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, hasError = true) }
            }
        }
    }

    fun setError() {
        _uiState.update { it.copy(isLoading = false, hasError = true) }
    }

    private fun toggleFavorite() {
        val current = _uiState.value.data ?: return
        viewModelScope.launch {
            locationRepository.updateFavorite(current.id, !current.isFavorite)
        }
    }
}
