package com.universe.explorer.domain.usecase

import com.universe.explorer.data.repository.CelestialBodyRepository
import com.universe.explorer.data.models.CelestialBody
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCelestialBodiesUseCase @Inject constructor(
    private val repository: CelestialBodyRepository
) {
    operator fun invoke(): Flow<List<CelestialBody>> = repository.getAllCelestialBodies()
}

class SearchCelestialBodiesUseCase @Inject constructor(
    private val repository: CelestialBodyRepository
) {
    operator fun invoke(query: String): Flow<List<CelestialBody>> =
        repository.searchCelestialBodies(query)
}

class GetCelestialBodiesByTypeUseCase @Inject constructor(
    private val repository: CelestialBodyRepository
) {
    operator fun invoke(type: String): Flow<List<CelestialBody>> =
        repository.getCelestialBodiesByType(type)
}

class GetAstronomyPictureOfDayUseCase @Inject constructor(
    private val repository: CelestialBodyRepository
) {
    suspend operator fun invoke(apiKey: String) =
        repository.getAstronomyPictureOfDay(apiKey)
}
