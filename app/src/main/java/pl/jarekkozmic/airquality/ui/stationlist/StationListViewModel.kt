package pl.jarekkozmic.airquality.ui.stationlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.jarekkozmic.airquality.logic.usecase.GetStationsUseCase
import javax.inject.Inject

@HiltViewModel
class StationListViewModel @Inject constructor(private val getStationUseCase: GetStationsUseCase) : ViewModel() {

    var state by mutableStateOf(
        State(stations = listOf())
    )

    init {
        viewModelScope.launch {
            loadStations()
        }
    }

    private suspend fun loadStations(){
        val stations = getStationUseCase.execute()
        state = State(stations.map { aqStation -> aqStation.name})
    }

    data class State(
        val stations: List<String> = listOf()
    )
}