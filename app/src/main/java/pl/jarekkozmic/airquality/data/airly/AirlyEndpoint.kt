package pl.jarekkozmic.airquality.data.airly

class AirlyEndpoint {
    companion object {
        const val HOST = "https://airapi.airly.eu/v2/"
        const val INSTALLATIONS = "installations/nearest?lat=50.062006&lng=19.940984&maxDistanceKM=5&maxResults=100"
    }
}