package pl.jarekkozmic.airquality.logic.repository

import pl.jarekkozmic.airquality.entity.AQStation

interface LocalStationsRepository {
    suspend fun getAll() : List<AQStation>
    suspend fun save(stations: List<AQStation>)
}