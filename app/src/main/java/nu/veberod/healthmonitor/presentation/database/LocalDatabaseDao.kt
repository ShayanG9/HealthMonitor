package nu.veberod.healthmonitor.presentation.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocalDatabaseDao {

    @Query("SELECT * FROM data")
    fun getAll(): List<LocalDatabase>

    @Query("SELECT * FROM data WHERE :sensor")
    suspend fun getHeartRate(sensor: String): LocalDatabase

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(localDatabase: LocalDatabase)

    @Query("DELETE FROM data")
    suspend fun deleteAll()

}