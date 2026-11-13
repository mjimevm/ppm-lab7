package com.example.lab7.ui.screens.Characters.List

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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

class CharacterListViewModel(application: Application) : AndroidViewModel(application) {
    private val characterRepository: CharacterRepository = CharacterRepositoryImpl(AppDatabase.getDatabase(application).characterDao())

    private val _uiState = MutableStateFlow(CharacterListUiState())
    val uiState: StateFlow<CharacterListUiState> = _uiState.asStateFlow()

    init {
        loadCharacters()
    }

    fun onEvent(event: CharacterListEvent) {
        when (event) {
            is CharacterListEvent.LoadCharacters -> loadCharacters()
            is CharacterListEvent.ToggleFavorite -> toggleFavorite(event.characterId, event.currentFavorite)
            is CharacterListEvent.SetFilter -> setFilter(event.filter)
            is CharacterListEvent.AddCharacter -> addCharacter(event.name, event.status, event.species, event.gender, event.image)
            is CharacterListEvent.DeleteCharacter -> deleteCharacter(event.characterId)
            is CharacterListEvent.UpdateCharacter -> updateCharacter(event.characterId, event.name, event.status, event.species, event.gender, event.image)
        }
    }

    private fun loadCharacters() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                characterRepository.characters.collectLatest { characters ->
                    _uiState.update { it.copy(isLoading = false, data = characters, hasError = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, hasError = true) }
            }
        }
    }

    private fun toggleFavorite(characterId: Int, currentFavorite: Boolean) {
        viewModelScope.launch {
            characterRepository.updateFavorite(characterId, !currentFavorite)
        }
    }

    private fun setFilter(filter: CharacterFilter) {
        _uiState.update { it.copy(filter = filter) }
    }

    private fun addCharacter(name: String, status: String, species: String, gender: String, image: String) {
        viewModelScope.launch {
            val maxId = characterRepository.getMaxId() ?: 1000
            val newId = maxId + 1
            val newChar = Character(
                id = newId,
                name = name,
                status = status,
                species = species,
                gender = gender,
                image = image.ifBlank { "https://rickandmortyapi.com/api/character/avatar/19.jpeg" },
                isFavorite = false
            )
            characterRepository.insertCharacter(newChar)
        }
    }

    private fun deleteCharacter(characterId: Int) {
        viewModelScope.launch {
            characterRepository.deleteCharacter(characterId)
        }
    }

    private fun updateCharacter(characterId: Int, name: String, status: String, species: String, gender: String, image: String) {
        viewModelScope.launch {
            val current = _uiState.value.data.find { it.id == characterId }
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
