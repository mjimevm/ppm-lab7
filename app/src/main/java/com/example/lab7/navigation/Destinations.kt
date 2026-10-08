package com.example.lab7.navigation

import kotlinx.serialization.Serializable

@Serializable
object LoginDestination

@Serializable
object AppGraph

@Serializable
object CharacterGraph

@Serializable
object CharacterListDestination

@Serializable
data class CharacterDetailDestination(val characterId: Int)

@Serializable
object LocationGraph

@Serializable
object LocationListDestination

@Serializable
data class LocationDetailDestination(val locationId: Int)

@Serializable
object ProfileDestination
