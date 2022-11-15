package pl.jarekkozmic.airquality.data.local

import pl.jarekkozmic.airquality.entity.AQStation
import pl.jarekkozmic.airquality.logic.repository.LocalStationsRepository

class InMemoryStationsRepository : LocalStationsRepository {
    override suspend fun getAll(): List<AQStation> {
        TODO("Not yet implemented")
    }

    override suspend fun save(stations: List<AQStation>) {
        TODO("Not yet implemented")
    }
}