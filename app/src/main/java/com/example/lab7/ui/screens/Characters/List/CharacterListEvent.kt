package com.example.lab7.ui.screens.Characters.List

sealed interface CharacterListEvent {
    data object LoadCharacters : CharacterListEvent
    data object SetError : CharacterListEvent
}
