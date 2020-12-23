package ru.itrequest.data

data class Waterfall(
    val id: Int,
    val name: String,
    val description: String,
    val notes: String,
    val openHours: String,
    val location: Location
)

data class Location(
    val lon: Double,
    val lat: Double
)

data class WaterfallResponse(
    val error: Int = 1,
    val payload: List<Waterfall>,
    val desc: String = ""
)