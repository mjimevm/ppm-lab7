package com.example.lab7.screens.Login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab7.core.DataPreferences.DataStorePreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SessionViewModel(application: Application) : AndroidViewModel(application) {
    private val sessionPreferences = DataStorePreferences(context = application)

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
            sessionPreferences.saveUserName(name)
            onComplete()
        }
    }

    fun clearSession(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            sessionPreferences.clearUserName()
            onComplete()
        }
    }
}
