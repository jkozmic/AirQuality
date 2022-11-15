package pl.jarekkozmic.airquality.data.local.db

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import pl.jarekkozmic.airquality.entity.AQStation
import pl.jarekkozmic.airquality.logic.repository.LocalStationsRepository
import javax.inject.Inject

class DatabaseStationsRepository @Inject constructor(private val database: AppDatabase) : LocalStationsRepository {
    override suspend fun getAll(): List<AQStation> {
        val stationsEntities = database.stationsDao().getAll()
        return stationsEntities.map{ AQStation(it.uid, it.name, it.city, it.sponsor, it.sponsorImage) }
    }

    override suspend fun save(stations: List<AQStation>) {
        database.stationsDao().insert(stations.map { StationEntity(it.id, it.name, it.city, it.sponsor, it.sponsorImage) })
    }
}

@Entity
data class StationEntity(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "sponsor") val sponsor: String,
    @ColumnInfo(name = "sponsor_image") val sponsorImage: String?
)

// Klasa typu Data Access Object odpowiadająca za bezpośrednie wykonywanie operacji bazodanowych dla danej encji
@Dao
interface  StationsDao{
    @Query("select * from stationentity")
    suspend fun getAll(): List<StationEntity>

    @Insert
    suspend fun insert(stations: List<StationEntity>)
}

//Lokalna baza danych
@Database(entities = [StationEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun stationsDao() : StationsDao
}