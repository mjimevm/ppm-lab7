package com.example.lab7.data.repository

import com.example.lab7.data.local.LocationDb
import com.example.lab7.data.local.dao.LocationDao
import com.example.lab7.data.remote.RickAndMortyApiService
import com.example.lab7.data.remote.RickAndMortyApiServiceImpl
import com.example.lab7.ui.screens.Locations.Classes.Location
import com.example.lab7.ui.screens.Locations.Classes.mapToEntity
import com.example.lab7.ui.screens.Locations.Classes.mapToModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LocationRepositoryImpl(
    private val locationDao: LocationDao,
    private val apiService: RickAndMortyApiService = RickAndMortyApiServiceImpl()
) : LocationRepository {

    override val locations: Flow<List<Location>> = locationDao.getLocations().map { entities ->
        entities.map { it.mapToModel() }
    }

    override fun getLocationById(id: Int): Flow<Location?> {
        return locationDao.getLocationById(id).map { it?.mapToModel() }
    }

    override suspend fun updateFavorite(id: Int, isFavorite: Boolean) {
        withContext(Dispatchers.IO) {
            locationDao.updateFavorite(id, isFavorite)
        }
    }

    override suspend fun syncWithApi() {
        withContext(Dispatchers.IO) {
            try {
                val locCount = locationDao.getLocationCount()
                if (locCount == 0) {
                    val apiLocations = apiService.getLocations()
                    val locationsToInsert = if (apiLocations.isNotEmpty()) {
                        apiLocations.map { dto ->
                            Location(
                                id = dto.id,
                                name = dto.name,
                                type = dto.type,
                                dimension = dto.dimension,
                                isFavorite = false
                            ).mapToEntity()
                        }
                    } else {
                        LocationDb.locations.map { it.mapToEntity() }
                    }
                    locationDao.insertLocations(locationsToInsert)
                }
            } catch (e: Exception) {
                if (locationDao.getLocationCount() == 0) {
                    locationDao.insertLocations(LocationDb.locations.map { it.mapToEntity() })
                }
            }
        }
    }
}
