package com.example.lab7.ui.screens.Characters.List

import com.example.lab7.ui.screens.Characters.Classes.Character

data class CharacterListUiState(
    val isLoading: Boolean = false,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false,
    val showOnlyFavorites: Boolean = false
)
