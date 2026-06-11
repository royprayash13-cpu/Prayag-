package com.universe.explorer.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val SETTINGS_DATASTORE = "settings_datastore"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_DATASTORE)

class PreferencesManager(private val context: Context) {

    companion object {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val GRAPHICS_QUALITY = stringPreferencesKey("graphics_quality")
        val CAMERA_DISTANCE = floatPreferencesKey("camera_distance")
        val RENDER_RESOLUTION = stringPreferencesKey("render_resolution")
        val ENABLE_SOUND = booleanPreferencesKey("enable_sound")
        val ENABLE_HAPTICS = booleanPreferencesKey("enable_haptics")
        val AUTO_ROTATE = booleanPreferencesKey("auto_rotate")
    }

    val darkModeFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DARK_MODE] ?: true
    }

    val graphicsQualityFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[GRAPHICS_QUALITY] ?: "MEDIUM"
    }

    val cameraDistanceFlow: Flow<Float> = context.dataStore.data.map { preferences ->
        preferences[CAMERA_DISTANCE] ?: 2.5f
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences().apply {
                this[DARK_MODE] = enabled
            }
        }
    }

    suspend fun setGraphicsQuality(quality: String) {
        context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences().apply {
                this[GRAPHICS_QUALITY] = quality
            }
        }
    }

    suspend fun setCameraDistance(distance: Float) {
        context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences().apply {
                this[CAMERA_DISTANCE] = distance
            }
        }
    }
}
