package com.example.lab7.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class RickAndMortyApiServiceImpl : RickAndMortyApiService {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(HttpTimeout) {
            connectTimeoutMillis = 5000
            requestTimeoutMillis = 5000
            socketTimeoutMillis = 5000
        }
    }

    override suspend fun getCharacters(): List<CharacterDto> {
        return try {
            val response: CharacterApiResponse = client.get("https://rickandmortyapi.com/api/character").body()
            response.results
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getLocations(): List<LocationDto> {
        return try {
            val response: LocationApiResponse = client.get("https://rickandmortyapi.com/api/location").body()
            response.results
        } catch (e: Exception) {
            emptyList()
        }
    }
}
