package com.example.lab7.ui.screens.Characters.Classes

import com.example.lab7.data.local.entity.CharacterEntity

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String,
    val isFavorite: Boolean = false
)

fun Character.mapToEntity(): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        image = image,
        isFavorite = isFavorite
    )
}

fun CharacterEntity.mapToModel(): Character {
    return Character(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        image = image,
        isFavorite = isFavorite
    )
}
