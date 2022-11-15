package pl.jarekkozmic.airquality.usecase

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import pl.jarekkozmic.airquality.entity.AQStation
import pl.jarekkozmic.airquality.logic.repository.LocalStationsRepository
import pl.jarekkozmic.airquality.logic.repository.RemoteStationsRepository
import pl.jarekkozmic.airquality.logic.usecase.GetStationsUseCase


class GetStationsUseCaseTest {
    //lateinit - zainicjalizujemy później
    lateinit var sut : GetStationsUseCase
    private lateinit var remote : MockRemoteStationsRepository
    private lateinit var local : MockLocalStationsRepository

    @Before
    fun setUp(){
        remote = MockRemoteStationsRepository();
        local = MockLocalStationsRepository()
        // sut - skrót system under test
        sut = GetStationsUseCase(remoteStationsRepository = remote, localStationsRepository = local)
    }

    @Test
    fun init_DoesNotMakeAnyRemoteOrLocalCalls(){
        assertEquals(false, remote.getAllCalled)
    }

    @Test
    fun executeMakeOneCollToLocal() = runBlocking {
        sut.execute()
        assertEquals(1, local.getAllCallsCount)
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

class MockLocalStationsRepository : LocalStationsRepository{

    val getAllCalled : Boolean
        get() = getAllCallsCount > 0
    var getAllCallsCount : Int = 0

    val saveAllCalled : Boolean
        get() = saveAllCallsCount > 0
    var saveAllCallsCount : Int = 0

    override suspend fun getAll(): List<AQStation> {
        getAllCallsCount++
        return listOf()
    }

    override suspend fun save(stations: List<AQStation>) {
        saveAllCallsCount++
    }

}