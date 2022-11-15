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

        local.getAllResults = listOf(sampleAQStation1)

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
        local.getAllResults = listOf(sampleAQStation1)
        sut.execute()
        assertEquals(1, local.getAllCallsCount)
    }

    @Test
    fun executeWhenRemoteStationsWhenRemoteStationRepositoryIsCalled() = runBlocking {
        local.getAllResults = emptyList()
        remote.getAllResults = listOf(sampleAQStation1)

        val actual  = sut.execute()

        assertEquals("1", actual.first().id)
    }

    @Test
    fun executeReturnsLocalStationsWhenLocalRepositoryIsNotEmpty() = runBlocking {
        local.getAllResults = listOf(sampleAQStation1)

        val actual  = sut.execute()

        assertEquals("1", actual.first().id)
    }

    @Test
    fun executeSaveStationsToLocalWhenRemoteIsNonEmpty() = runBlocking {
        local.getAllResults = emptyList();
        remote.getAllResults = listOf(sampleAQStation1)

        sut.execute()

        assertEquals(true, local.saveCalled)
        assertEquals("1", local.saveReceivedArguments.first().id)
    }

    @Test
    fun executeReturnsValidLocalListStations() = runBlocking {
        val sampleAQStation2 = AQStation("2", "", "", "", "")
        local.getAllResults = listOf(sampleAQStation1, sampleAQStation2)

        val actual = sut.execute()

        assertEquals("1", actual.first().id)
        assertEquals("2", actual[1].id)
    }

    var sampleAQStation1 = AQStation("1", "", "", "", "")
}

class MockRemoteStationsRepository() : RemoteStationsRepository{

    //parametr sprawdzający czy funkcja getAllCalled została wywołana
    val getAllCalled : Boolean
        get() = getAllCallsCount > 0
    //parametr sprawdzający ile razy funkcja getAllCalled została wywołana (sprawdzanie scenariuszy brzegowych
    //przydatne aby zabezpieczyć przed błędami przy ewentualnej modyfikacji metody)
    var getAllCallsCount : Int = 0
    var getAllResults : List<AQStation> = emptyList()

    override suspend fun getAll(): List<AQStation> {
        getAllCallsCount++
        return getAllResults
    }
}

class MockLocalStationsRepository : LocalStationsRepository{

    //Parametry analogicznie do MockRemote
    val getAllCalled: Boolean
        get() = getAllCallsCount > 0
    var getAllCallsCount: Int = 0

    val saveCalled: Boolean
        get() = saveCallsCount > 0
    var saveCallsCount: Int = 0
    var getAllResults: List<AQStation> = emptyList()
    var saveReceivedArguments: List<AQStation> = emptyList()

    override suspend fun getAll(): List<AQStation> {
        getAllCallsCount++
        return getAllResults
    }

    override suspend fun save(stations: List<AQStation>) {
        saveCallsCount++
        saveReceivedArguments = stations
    }

}