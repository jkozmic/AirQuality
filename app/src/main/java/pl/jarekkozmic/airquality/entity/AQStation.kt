package pl.jarekkozmic.airquality.entity

// klasa encji, nie zawiera żadnych importów i nie zależy od niczego
data class AQStation(
    val id: String,
    val name: String,
    val city: String,
    val sponsor: String,
    val sponsorImage: String?,
//    val isAirlyStation: Boolean
)
