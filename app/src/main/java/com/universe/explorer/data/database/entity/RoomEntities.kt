package com.universe.explorer.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "celestial_bodies")
data class CelestialBodyEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val type: String,
    val description: String,
    val diameter: Double,
    val mass: Double,
    val temperature: Double,
    val distance: Double,
    val textureUrl: String,
    val normalMapUrl: String,
    val atmosphereColor: String,
    val orbitalPeriod: Double? = null,
    val rotationPeriod: Double? = null,
    val discoveryDate: String? = null,
    val imageUrl: String? = null,
    val cached: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey
    val id: String,
    val celestialBodyId: String,
    val favoriteType: String,
    val savedAt: Long = System.currentTimeMillis(),
    val notes: String? = null
)
