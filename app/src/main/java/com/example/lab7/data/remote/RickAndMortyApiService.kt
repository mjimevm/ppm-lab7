package com.example.lab7.data.remote

interface RickAndMortyApiService {
    suspend fun getCharacters(): List<CharacterDto>
    suspend fun getLocations(): List<LocationDto>
}
