package com.universe.explorer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.universe.explorer.data.database.entity.CelestialBodyEntity
import com.universe.explorer.data.database.entity.FavoriteEntity
import com.universe.explorer.data.database.dao.CelestialBodyDao
import com.universe.explorer.data.database.dao.FavoriteDao

@Database(
    entities = [
        CelestialBodyEntity::class,
        FavoriteEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class CelestialDatabase : RoomDatabase() {
    abstract fun celestialBodyDao(): CelestialBodyDao
    abstract fun favoriteDao(): FavoriteDao
}
