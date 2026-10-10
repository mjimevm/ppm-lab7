package com.example.lab7.screens.Login

sealed interface SessionEvent {
    data class SaveUserName(val name: String, val onComplete: () -> Unit = {}) : SessionEvent
    data class ClearSession(val onComplete: () -> Unit = {}) : SessionEvent
}
