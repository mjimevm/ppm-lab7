package com.example.lab7.ui.screens.Login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab7.core.DataPreferences.DataStorePreferences
import com.example.lab7.data.local.AppDatabase
import com.example.lab7.data.repository.CharacterRepository
import com.example.lab7.data.repository.CharacterRepositoryImpl
import com.example.lab7.data.repository.LocationRepository
import com.example.lab7.data.repository.LocationRepositoryImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SessionViewModel(application: Application) : AndroidViewModel(application) {
    private val sessionPreferences = DataStorePreferences(context = application)
    private val database = AppDatabase.getDatabase(application)
    private val characterRepository: CharacterRepository = CharacterRepositoryImpl(database.characterDao())
    private val locationRepository: LocationRepository = LocationRepositoryImpl(database.locationDao())

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    val userName: StateFlow<String?> = sessionPreferences.userNameFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val uiState: StateFlow<SessionState> = userName
        .map { SessionState(userName = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SessionState()
        )

    fun onEvent(event: SessionEvent) {
        when (event) {
            is SessionEvent.SaveUserName -> saveUserName(event.name, event.onComplete)
            is SessionEvent.ClearSession -> clearSession(event.onComplete)
        }
    }

    fun saveUserName(name: String, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            _isSyncing.value = true
            try {
                characterRepository.syncWithApi()
                locationRepository.syncWithApi()
                delay(4000)
                sessionPreferences.saveUserName(name)
                onComplete()
            } finally {
                _isSyncing.value = false
            }
        }
    }

    fun clearSession(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            sessionPreferences.clearUserName()
            database.characterDao().deleteAllCharacters()
            database.locationDao().deleteAllLocations()
            onComplete()
        }
    }
}
