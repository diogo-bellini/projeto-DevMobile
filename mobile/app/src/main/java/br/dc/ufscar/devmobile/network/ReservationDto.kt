package br.dc.ufscar.devmobile.network

data class ReservationRequest(
    val storeId: Int,
    val date: String,
    val time: String,
    val people: Int,
    val table: Int,
    val createdAt: String
)

data class ReservationResponse(
    val id: Int,
    val storeId: Int,
    val date: String,
    val time: String,
    val people: Int,
    val table: Int,
    val createdAt: String
)
