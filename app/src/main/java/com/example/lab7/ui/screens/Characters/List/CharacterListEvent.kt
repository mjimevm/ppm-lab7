package com.example.lab7.ui.screens.Characters.List

sealed interface CharacterListEvent {
    data object LoadCharacters : CharacterListEvent
    data class ToggleFavorite(val characterId: Int, val currentFavorite: Boolean) : CharacterListEvent
    data object ToggleFilterFavorites : CharacterListEvent
}
