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
        State(stations = listOf(), isRefreshing = true)
    )

    init {
        loadStations()
    }

    fun onPullToRefresh() {
        loadStations()
    }

    private fun loadStations() {
        viewModelScope.launch {
            val stations = getStationUseCase.execute()
            state = State(stations.map { aqStation ->
                StationViewData(
                    title = aqStation.name,
                    subtitle = aqStation.city,
                    imageUrl = aqStation.sponsorImage,
                    label = aqStation.sponsor
                ) },
                isRefreshing = false)
        }
    }

    data class State(
        val stations: List<StationViewData> = listOf(),
        val isRefreshing: Boolean = false
    )

    data class StationViewData(
        val title: String,
        val subtitle: String,
        val imageUrl: String?,
        val label: String
    )
}