package com.example.lab7.ui.screens.Characters.Detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.lab7.core.navigation.CharacterDetailDestination
import com.example.lab7.data.local.AppDatabase
import com.example.lab7.data.repository.CharacterRepository
import com.example.lab7.data.repository.CharacterRepositoryImpl
import com.example.lab7.ui.screens.Characters.Classes.Character
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

    private val characterRepository: CharacterRepository = CharacterRepositoryImpl(AppDatabase.getDatabase(application).characterDao())

    private val _uiState = MutableStateFlow(CharacterDetailUiState())
    val uiState: StateFlow<CharacterDetailUiState> = _uiState.asStateFlow()

    init {
        loadCharacter()
    }

    fun onEvent(event: CharacterDetailEvent) {
        when (event) {
            is CharacterDetailEvent.LoadCharacter -> loadCharacter()
            is CharacterDetailEvent.ToggleFavorite -> toggleFavorite()
            is CharacterDetailEvent.DeleteCharacter -> deleteCharacter(event.characterId)
            is CharacterDetailEvent.UpdateCharacter -> updateCharacter(event.characterId, event.name, event.status, event.species, event.gender, event.image)
        }
    }

    private fun loadCharacter() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                characterRepository.getCharacterById(characterId).collectLatest { character ->
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
            characterRepository.updateFavorite(currentCharacter.id, !currentCharacter.isFavorite)
        }
    }

    private fun deleteCharacter(characterId: Int) {
        viewModelScope.launch {
            characterRepository.deleteCharacter(characterId)
        }
    }

    private fun updateCharacter(characterId: Int, name: String, status: String, species: String, gender: String, image: String) {
        viewModelScope.launch {
            val current = _uiState.value.data
            val isFav = current?.isFavorite ?: false
            val updated = Character(
                id = characterId,
                name = name,
                status = status,
                species = species,
                gender = gender,
                image = image.ifBlank { current?.image ?: "https://rickandmortyapi.com/api/character/avatar/19.jpeg" },
                isFavorite = isFav
            )
            characterRepository.insertCharacter(updated)
        }
    }
}
