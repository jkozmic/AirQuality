package pl.jarekkozmic.airquality.data.local

import pl.jarekkozmic.airquality.entity.AQStation
import pl.jarekkozmic.airquality.logic.repository.LocalStationsRepository

class InMemoryStationsRepository : LocalStationsRepository {
    companion object {
        private var stations: List<AQStation> = emptyList()
    }


    override suspend fun getAll(): List<AQStation> {
        return stations
    }

    override suspend fun save(stations: List<AQStation>) {
        InMemoryStationsRepository.stations = stations
    }
}