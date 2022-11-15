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
    fun executeMakeOneCallToLocal() = runBlocking {
        sut.execute()
        assertEquals(1, local.getAllCallsCount)
    }

    @Test
    fun executeMakesCallToRemoteWhenLocalDataIsEmpty() = runBlocking{
        local.getAllResults = emptyList()

        sut.execute()

        assertEquals(true, remote.getAllCalled)
    }

    @Test
    fun executeDoesNotMakesCallToRemoteWhenLocalDataIsNonEmpty() = runBlocking{

        local.getAllResults = listOf(sampleAQStation)

        sut.execute()

        assertEquals(false, remote.getAllCalled)
    }

    @Test
    fun executeDoesMakeOneCallToLocal() = runBlocking {
        sut.execute()
        assertEquals(1, local.getAllCallsCount)
    }

    @Test
    fun executeDoesMakeOneCallToLocalForNonEmptyData() = runBlocking {
        local.getAllResults = listOf(sampleAQStation)
        sut.execute()
        assertEquals(1, local.getAllCallsCount)
    }

    @Test
    fun executeWhenRemoteStationsWhenRemoteStationRepositoryIsCalled() = runBlocking {
        local.getAllResults = emptyList()
        remote.getAllResults = listOf(sampleAQStation)

        val actual  = sut.execute()

        assertEquals("1", actual.first().id)
    }

    @Test
    fun executeReturnsLocalStationsWhenLocalRepositoryIsNotEmpty() = runBlocking {
        local.getAllResults = listOf(sampleAQStation)

        val actual  = sut.execute()

        assertEquals("1", actual.first().id)
    }

    var sampleAQStation = AQStation("1", "", "", "", "")
}

class MockRemoteStationsRepository() : RemoteStationsRepository{

    val getAllCalled : Boolean
        get() = getAllCallsCount > 0
    var getAllCallsCount : Int = 0
    var getAllResults : List<AQStation> = emptyList()

    override suspend fun getAll(): List<AQStation> {
        getAllCallsCount++
        return getAllResults
    }
}

class MockLocalStationsRepository : LocalStationsRepository{

    val getAllCalled : Boolean
        get() = getAllCallsCount > 0
    var getAllCallsCount : Int = 0

    val saveAllCalled : Boolean
        get() = saveAllCallsCount > 0
    var saveAllCallsCount : Int = 0
    var getAllResults : List<AQStation> = emptyList()


    override suspend fun getAll(): List<AQStation> {
        getAllCallsCount++
        return getAllResults
    }

    override suspend fun save(stations: List<AQStation>) {
        saveAllCallsCount++
    }

}