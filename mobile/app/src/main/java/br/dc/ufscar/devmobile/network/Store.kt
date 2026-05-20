package br.dc.ufscar.devmobile.network

data class Store(
    val id: Int,
    val name: String,
    val namepiece: String,
    val address: String,
    val logo: String,
    val rating: Double,
    val reviews: Int,
    val avgPrice: Double,
    val category: String,
    val time: String,
    val latitude: Double,
    val longitude: Double,
    val openHour: Int,
    val closeHour: Int
)
