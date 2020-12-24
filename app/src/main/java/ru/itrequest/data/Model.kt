package ru.itrequest.data

data class Waterfall(
    val id: Int,
    val name: String,
    val description: String,
    val notes: String,
    val openHours: String,
    val address: String
)

data class WaterfallResponse(
    val error: Int,
    val payload: List<Waterfall>,
    val desc: String
)