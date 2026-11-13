package com.example.lab7.ui.screens.Locations.Classes

import com.example.lab7.data.local.entity.LocationEntity

data class Location(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val isFavorite: Boolean = false
)

fun Location.mapToEntity(): LocationEntity {
    return LocationEntity(
        id = id,
        name = name,
        type = type,
        dimension = dimension,
        isFavorite = isFavorite
    )
}

fun LocationEntity.mapToModel(): Location {
    return Location(
        id = id,
        name = name,
        type = type,
        dimension = dimension,
        isFavorite = isFavorite
    )
}
