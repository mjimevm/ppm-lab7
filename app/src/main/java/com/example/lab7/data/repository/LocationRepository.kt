package com.example.lab7.data.repository

import com.example.lab7.ui.screens.Locations.Classes.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    val locations: Flow<List<Location>>
    fun getLocationById(id: Int): Flow<Location?>
    suspend fun updateFavorite(id: Int, isFavorite: Boolean)
    suspend fun syncWithApi()
}
