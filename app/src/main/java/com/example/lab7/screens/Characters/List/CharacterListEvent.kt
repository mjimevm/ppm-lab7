package com.example.lab7.screens.Characters.List

sealed interface CharacterListEvent {
    data object LoadCharacters : CharacterListEvent
    data object SetError : CharacterListEvent
}
