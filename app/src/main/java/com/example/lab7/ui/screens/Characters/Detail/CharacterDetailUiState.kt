package com.example.lab7.ui.screens.Characters.Detail

import com.example.lab7.ui.screens.Characters.Classes.Character

data class CharacterDetailUiState(
    val isLoading: Boolean = true,
    val data: com.example.lab7.ui.screens.Characters.Classes.Character? = null,
    val hasError: Boolean = false
)
