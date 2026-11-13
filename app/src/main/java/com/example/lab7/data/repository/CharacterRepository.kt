package com.example.lab7.data.repository

import com.example.lab7.ui.screens.Characters.Classes.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    val characters: Flow<List<Character>>
    fun getCharacterById(id: Int): Flow<Character?>
    suspend fun insertCharacter(character: Character)
    suspend fun deleteCharacter(id: Int)
    suspend fun updateFavorite(id: Int, isFavorite: Boolean)
    suspend fun getMaxId(): Int?
    suspend fun syncWithApi()
}
