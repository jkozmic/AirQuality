package pl.jarekkozmic.airquality.logic.repository

import pl.jarekkozmic.airquality.entity.AQStation

interface RemoteStationsRepository {
    suspend fun getAll() : List<AQStation>
}