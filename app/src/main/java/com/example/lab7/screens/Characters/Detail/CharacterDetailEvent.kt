package com.example.lab7.screens.Characters.Detail

sealed interface CharacterDetailEvent {
    data object LoadCharacter : CharacterDetailEvent
    data object SetError : CharacterDetailEvent
}
