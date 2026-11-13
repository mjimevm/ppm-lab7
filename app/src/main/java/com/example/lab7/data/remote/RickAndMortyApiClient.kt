package com.example.lab7.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class CharacterApiResponse(val results: List<CharacterDto> = emptyList())

@Serializable
data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String
)

@Serializable
data class LocationApiResponse(val results: List<LocationDto> = emptyList())

@Serializable
data class LocationDto(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String
)

object RickAndMortyApiClient {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun getCharacters(): List<CharacterDto> {
        return try {
            val response: CharacterApiResponse = client.get("https://rickandmortyapi.com/api/character").body()
            response.results
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getLocations(): List<LocationDto> {
        return try {
            val response: LocationApiResponse = client.get("https://rickandmortyapi.com/api/location").body()
            response.results
        } catch (e: Exception) {
            emptyList()
        }
    }
}
