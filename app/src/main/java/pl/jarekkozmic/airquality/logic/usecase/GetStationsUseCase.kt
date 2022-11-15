package pl.jarekkozmic.airquality.logic.usecase

import pl.jarekkozmic.airquality.entity.AQStation
import pl.jarekkozmic.airquality.logic.repository.LocalStationsRepository
import pl.jarekkozmic.airquality.logic.repository.RemoteStationsRepository
import javax.inject.Inject

class GetStationsUseCase @Inject constructor(
    private val remoteStationsRepository: RemoteStationsRepository,
    private val localStationsRepository: LocalStationsRepository
){

    suspend fun execute() : List<AQStation>{
        return  localStationsRepository.getAll()
    }
}