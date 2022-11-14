package pl.jarekkozmic.airquality.logic

import pl.jarekkozmic.airquality.entity.AQStation
import javax.inject.Singleton

@Singleton
class FakeRemoteStationsRepository : RemoteStationsRepository {
    override suspend fun getAll(): List<AQStation> {
       return emptyList<AQStation>()
    }
}