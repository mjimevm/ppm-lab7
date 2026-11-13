package com.example.lab7.ui.screens.Characters.List

enum class CharacterFilter {
    ALL, FAVORITES, NON_FAVORITES
}

sealed interface CharacterListEvent {
    data object LoadCharacters : CharacterListEvent
    data class ToggleFavorite(val characterId: Int, val currentFavorite: Boolean) : CharacterListEvent
    data class SetFilter(val filter: CharacterFilter) : CharacterListEvent
    data class AddCharacter(val name: String, val status: String, val species: String, val gender: String, val image: String) : CharacterListEvent
    data class DeleteCharacter(val characterId: Int) : CharacterListEvent
    data class UpdateCharacter(val characterId: Int, val name: String, val status: String, val species: String, val gender: String, val image: String) : CharacterListEvent
}
