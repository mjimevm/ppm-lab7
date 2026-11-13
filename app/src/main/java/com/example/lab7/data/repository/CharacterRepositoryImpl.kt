package com.example.lab7.data.repository

import com.example.lab7.data.local.CharacterDb
import com.example.lab7.data.local.dao.CharacterDao
import com.example.lab7.data.remote.RickAndMortyApiService
import com.example.lab7.data.remote.RickAndMortyApiServiceImpl
import com.example.lab7.ui.screens.Characters.Classes.Character
import com.example.lab7.ui.screens.Characters.Classes.mapToEntity
import com.example.lab7.ui.screens.Characters.Classes.mapToModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CharacterRepositoryImpl(
    private val characterDao: CharacterDao,
    private val apiService: RickAndMortyApiService = RickAndMortyApiServiceImpl()
) : CharacterRepository {

    override val characters: Flow<List<Character>> = characterDao.getCharacters().map { entities ->
        entities.map { it.mapToModel() }
    }

    override fun getCharacterById(id: Int): Flow<Character?> {
        return characterDao.getCharacterById(id).map { it?.mapToModel() }
    }

    override suspend fun insertCharacter(character: Character) {
        withContext(Dispatchers.IO) {
            characterDao.insertCharacter(character.mapToEntity())
        }
    }

    override suspend fun deleteCharacter(id: Int) {
        withContext(Dispatchers.IO) {
            characterDao.deleteCharacter(id)
        }
    }

    override suspend fun updateFavorite(id: Int, isFavorite: Boolean) {
        withContext(Dispatchers.IO) {
            characterDao.updateFavorite(id, isFavorite)
        }
    }

    override suspend fun getMaxId(): Int? {
        return withContext(Dispatchers.IO) {
            characterDao.getMaxId()
        }
    }

    override suspend fun syncWithApi() {
        withContext(Dispatchers.IO) {
            try {
                val charCount = characterDao.getCharacterCount()
                if (charCount == 0) {
                    val apiCharacters = apiService.getCharacters()
                    val charactersToInsert = if (apiCharacters.isNotEmpty()) {
                        apiCharacters.map { dto ->
                            Character(
                                id = dto.id,
                                name = dto.name,
                                status = dto.status,
                                species = dto.species,
                                gender = dto.gender,
                                image = dto.image,
                                isFavorite = false
                            ).mapToEntity()
                        }
                    } else {
                        CharacterDb.characters.map { it.mapToEntity() }
                    }
                    characterDao.insertCharacters(charactersToInsert)
                }
            } catch (e: Exception) {
                if (characterDao.getCharacterCount() == 0) {
                    characterDao.insertCharacters(CharacterDb.characters.map { it.mapToEntity() })
                }
            }
        }
    }
}
