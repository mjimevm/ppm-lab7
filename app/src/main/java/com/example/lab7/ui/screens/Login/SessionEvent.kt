package com.example.lab7.ui.screens.Login

sealed interface SessionEvent {
    data class SaveUserName(val name: String, val onComplete: () -> Unit = {}) : SessionEvent
    data class ClearSession(val onComplete: () -> Unit = {}) : SessionEvent
}
