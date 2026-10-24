package com.example.lab7.ui.screens.Locations.List

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab7.data.local.AppDatabase
import com.example.lab7.ui.screens.Locations.Classes.mapToModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationListViewModel(application: Application) : AndroidViewModel(application) {
    private val locationDao = AppDatabase.getDatabase(application).locationDao()

    private val _uiState = MutableStateFlow(LocationListUiState())
    val uiState: StateFlow<LocationListUiState> = _uiState.asStateFlow()

    init {
        loadLocations()
    }

    fun onEvent(event: LocationListEvent) {
        when (event) {
            is LocationListEvent.LoadLocations -> loadLocations()
            is LocationListEvent.SetError -> setError()
        }
    }

    fun loadLocations() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, hasError = false) }
            try {
                locationDao.getLocations().collectLatest { entities ->
                    val locations = entities.map { it.mapToModel() }
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
}
