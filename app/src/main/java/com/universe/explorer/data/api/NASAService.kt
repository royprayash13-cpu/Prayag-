package com.universe.explorer.data.api

import com.universe.explorer.data.models.AstronomyMedia
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service for NASA API integration
 */
interface NASAService {

    /**
     * Get Astronomy Picture of the Day
     */
    @GET("planetary/apod")
    suspend fun getAstronomyPictureOfDay(
        @Query("api_key") apiKey: String,
        @Query("count") count: Int = 1,
        @Query("hd") hd: Boolean = true
    ): List<AstronomyMedia>

    /**
     * Get near-Earth objects data
     */
    @GET("neo/rest/v1/neo/browse")
    suspend fun getNearEarthObjects(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 0
    ): String // Parse JSON response

    /**
     * Get Mars rover imagery
     */
    @GET("mars-photos/api/v1/rovers/curiosity/latest_photos")
    suspend fun getMarsRoverPhotos(
        @Query("api_key") apiKey: String
    ): String // Parse JSON response
}
