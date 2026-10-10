package com.example.lab7.screens.Characters.Detail

import com.example.lab7.screens.Characters.Classes.Character

data class CharacterDetailUiState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false
)
