package pl.jarekkozmic.airquality.usecase

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.jarekkozmic.airquality.entity.AQStation
import pl.jarekkozmic.airquality.logic.repository.RemoteStationsRepository
import pl.jarekkozmic.airquality.logic.usecase.GetStationsUseCase


class GetStationsUseCaseTest {

    @Test
    fun init_DoesNotMakeAnyRemoteOrLocalCalls(){
        // sut - skrót system under test

        //GIVEN
        val remote = MockRemoteStationsRepository()
        val sut = GetStationsUseCase(remoteStationsRepository = remote)

        //THEN
        assertEquals(false, remote.getAllCalled)
    }

    @Test
    fun executeMakesOneCallToRemote() = runBlocking {
        //GIVEN
        val remote = MockRemoteStationsRepository()
        val sut = GetStationsUseCase(remoteStationsRepository = remote)

        //WHEN
        sut.execute()

        //THEN
        assertEquals(true, remote.getAllCalled)
        assertEquals(1, remote.getAllCallsCount)
    }
}

class MockRemoteStationsRepository() : RemoteStationsRepository{

    val getAllCalled : Boolean
        get() = getAllCallsCount > 0
    var getAllCallsCount : Int = 0

    override suspend fun getAll(): List<AQStation> {
        getAllCallsCount++
        return listOf()
    }

}