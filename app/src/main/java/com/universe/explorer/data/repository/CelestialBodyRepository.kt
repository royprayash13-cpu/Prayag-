package com.universe.explorer.data.repository

import com.universe.explorer.data.api.NASAService
import com.universe.explorer.data.database.dao.CelestialBodyDao
import com.universe.explorer.data.database.entity.CelestialBodyEntity
import com.universe.explorer.data.models.CelestialBody
import com.universe.explorer.data.models.CelestialBodyType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CelestialBodyRepository @Inject constructor(
    private val dao: CelestialBodyDao,
    private val nasaService: NASAService
) {

    fun getAllCelestialBodies(): Flow<List<CelestialBody>> = dao.getAll().map { entities ->
        entities.map { it.toDomain() }
    }

    fun getCelestialBodiesByType(type: String): Flow<List<CelestialBody>> =
        dao.getByType(type).map { entities ->
            entities.map { it.toDomain() }
        }

    fun searchCelestialBodies(query: String): Flow<List<CelestialBody>> =
        dao.search("%$query%").map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun getCelestialBodyById(id: String): CelestialBody? =
        dao.getById(id)?.toDomain()

    suspend fun cacheCelestialBodies(bodies: List<CelestialBody>) {
        dao.insertAll(bodies.map { it.toEntity() })
    }

    suspend fun getAstronomyPictureOfDay(apiKey: String) =
        nasaService.getAstronomyPictureOfDay(apiKey)

    private fun CelestialBodyEntity.toDomain() = CelestialBody(
        id = id,
        name = name,
        type = CelestialBodyType.valueOf(type),
        description = description,
        diameter = diameter,
        mass = mass,
        temperature = temperature,
        distance = distance,
        textureUrl = textureUrl,
        normalMapUrl = normalMapUrl,
        atmosphereColor = atmosphereColor,
        orbitalPeriod = orbitalPeriod,
        rotationPeriod = rotationPeriod,
        discoveryDate = discoveryDate,
        imageUrl = imageUrl
    )

    private fun CelestialBody.toEntity() = CelestialBodyEntity(
        id = id,
        name = name,
        type = type.name,
        description = description,
        diameter = diameter,
        mass = mass,
        temperature = temperature,
        distance = distance,
        textureUrl = textureUrl,
        normalMapUrl = normalMapUrl,
        atmosphereColor = atmosphereColor,
        orbitalPeriod = orbitalPeriod,
        rotationPeriod = rotationPeriod,
        discoveryDate = discoveryDate,
        imageUrl = imageUrl
    )
}
