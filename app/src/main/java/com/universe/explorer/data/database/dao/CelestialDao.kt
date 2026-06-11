package com.universe.explorer.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.universe.explorer.data.database.entity.CelestialBodyEntity
import com.universe.explorer.data.database.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CelestialBodyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(celestialBody: CelestialBodyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(celestialBodies: List<CelestialBodyEntity>)

    @Query("SELECT * FROM celestial_bodies WHERE id = :id")
    suspend fun getById(id: String): CelestialBodyEntity?

    @Query("SELECT * FROM celestial_bodies WHERE type = :type")
    fun getByType(type: String): Flow<List<CelestialBodyEntity>>

    @Query("SELECT * FROM celestial_bodies ORDER BY name ASC")
    fun getAll(): Flow<List<CelestialBodyEntity>>

    @Query("SELECT * FROM celestial_bodies WHERE name LIKE :query ORDER BY name ASC")
    fun search(query: String): Flow<List<CelestialBodyEntity>>

    @Delete
    suspend fun delete(celestialBody: CelestialBodyEntity)

    @Query("DELETE FROM celestial_bodies")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM celestial_bodies")
    suspend fun count(): Int
}

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity)

    @Query("SELECT * FROM favorites")
    fun getAll(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites WHERE celestialBodyId = :bodyId")
    suspend fun getByBodyId(bodyId: String): FavoriteEntity?

    @Delete
    suspend fun delete(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE celestialBodyId = :bodyId")
    suspend fun deleteByBodyId(bodyId: String)
}
