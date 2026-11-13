package com.example.lab7.ui.screens.Characters.Detail

sealed interface CharacterDetailEvent {
    data object LoadCharacter : CharacterDetailEvent
    data object ToggleFavorite : CharacterDetailEvent
    data class DeleteCharacter(val characterId: Int) : CharacterDetailEvent
    data class UpdateCharacter(val characterId: Int, val name: String, val status: String, val species: String, val gender: String, val image: String) : CharacterDetailEvent
}
