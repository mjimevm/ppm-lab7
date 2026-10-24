package com.example.lab7.ui.screens.Characters.Detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.lab7.core.navigation.CharacterDetailDestination
import com.example.lab7.data.local.AppDatabase
import com.example.lab7.ui.screens.Characters.Classes.mapToModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterDetailViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    private val characterId: Int = try {
        savedStateHandle.toRoute<CharacterDetailDestination>().characterId
    } catch (e: Exception) {
        1
    }

    private val characterDao = AppDatabase.getDatabase(application).characterDao()

    private val _uiState = MutableStateFlow(CharacterDetailUiState())
    val uiState: StateFlow<CharacterDetailUiState> = _uiState.asStateFlow()

    init {
        loadCharacter()
    }

    fun onEvent(event: CharacterDetailEvent) {
        when (event) {
            is CharacterDetailEvent.LoadCharacter -> loadCharacter()
            is CharacterDetailEvent.ToggleFavorite -> toggleFavorite()
        }
    }

    private fun loadCharacter() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                characterDao.getCharacterById(characterId).collectLatest { entity ->
                    val character = entity?.mapToModel()
                    _uiState.update { it.copy(isLoading = false, data = character, hasError = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, hasError = true) }
            }
        }
    }

    private fun toggleFavorite() {
        val currentCharacter = _uiState.value.data ?: return
        viewModelScope.launch {
            characterDao.updateFavorite(currentCharacter.id, !currentCharacter.isFavorite)
        }
    }
}
