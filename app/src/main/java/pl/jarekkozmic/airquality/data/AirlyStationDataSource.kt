package pl.jarekkozmic.airquality.data

import pl.jarekkozmic.airquality.data.airly.AirlyMapper
import pl.jarekkozmic.airquality.data.airly.AirlyService
import pl.jarekkozmic.airquality.entity.AQStation
import pl.jarekkozmic.airquality.logic.repository.RemoteStationsRepository
import javax.inject.Inject

class AirlyStationDataSource @Inject constructor(private val airlyService: AirlyService) : RemoteStationsRepository {
    override suspend fun getAll(): List<AQStation> {
        val installations = airlyService.getInstallations()
        return installations.map { AirlyMapper().mapInstallation(it)
        }
    }
}