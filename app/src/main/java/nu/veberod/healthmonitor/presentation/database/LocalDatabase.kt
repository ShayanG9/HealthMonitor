package nu.veberod.healthmonitor.presentation.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data")
data class LocalDatabase(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "sensor") val sensor: String,
    @ColumnInfo(name = "datapoint") val heartRate: Float,
)


