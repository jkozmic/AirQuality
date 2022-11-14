package pl.jarekkozmic.airquality.logic

import pl.jarekkozmic.airquality.entity.AQStation

interface RemoteStationsRepository {
    suspend fun getAll() : List<AQStation>
}