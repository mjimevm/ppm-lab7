package com.example.lab7.screens.Characters.List

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab7.screens.Characters.Classes.CharacterDb
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CharacterListUiState())
    val uiState: StateFlow<CharacterListUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        onEvent(CharacterListEvent.LoadCharacters)
    }

    fun onEvent(event: CharacterListEvent) {
        when (event) {
            is CharacterListEvent.LoadCharacters -> loadCharacters()
            is CharacterListEvent.SetError -> setError()
        }
    }

    fun loadCharacters() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, hasError = false) }
            delay(4000)
            try {
                val characters = CharacterDb().getAllCharacters()
                _uiState.update { it.copy(isLoading = false, data = characters, hasError = false) }
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
