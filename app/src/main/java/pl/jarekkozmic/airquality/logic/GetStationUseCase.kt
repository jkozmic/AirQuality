package pl.jarekkozmic.airquality.logic

import pl.jarekkozmic.airquality.entity.AQStation
import javax.inject.Inject

class GetStationUseCase @Inject constructor(
    private val remoteStationsRepository: RemoteStationsRepository
){

    suspend fun execute() : List<AQStation>{
        return  remoteStationsRepository.getAll();
    }
}