package com.example.lab7.ui.screens.Characters.List

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab7.data.local.AppDatabase
import com.example.lab7.ui.screens.Characters.Classes.mapToModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterListViewModel(application: Application) : AndroidViewModel(application) {
    private val characterDao = AppDatabase.getDatabase(application).characterDao()

    private val _uiState = MutableStateFlow(CharacterListUiState())
    val uiState: StateFlow<CharacterListUiState> = _uiState.asStateFlow()

    init {
        loadCharacters()
    }

    fun onEvent(event: CharacterListEvent) {
        when (event) {
            is CharacterListEvent.LoadCharacters -> loadCharacters()
            is CharacterListEvent.ToggleFavorite -> toggleFavorite(event.characterId, event.currentFavorite)
            is CharacterListEvent.ToggleFilterFavorites -> toggleFilterFavorites()
        }
    }

    private fun loadCharacters() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                characterDao.getCharacters().collectLatest { entities ->
                    val characters = entities.map { it.mapToModel() }
                    _uiState.update { it.copy(isLoading = false, data = characters, hasError = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, hasError = true) }
            }
        }
    }

    private fun toggleFavorite(characterId: Int, currentFavorite: Boolean) {
        viewModelScope.launch {
            characterDao.updateFavorite(characterId, !currentFavorite)
        }
    }

    private fun toggleFilterFavorites() {
        _uiState.update { it.copy(showOnlyFavorites = !it.showOnlyFavorites) }
    }
}
