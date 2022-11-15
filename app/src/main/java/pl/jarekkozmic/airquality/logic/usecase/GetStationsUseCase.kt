package pl.jarekkozmic.airquality.logic.usecase

import pl.jarekkozmic.airquality.entity.AQStation
import pl.jarekkozmic.airquality.logic.repository.RemoteStationsRepository
import javax.inject.Inject

class GetStationsUseCase @Inject constructor(
    private val remoteStationsRepository: RemoteStationsRepository
){

    suspend fun execute() : List<AQStation>{
        return  remoteStationsRepository.getAll();
    }
}