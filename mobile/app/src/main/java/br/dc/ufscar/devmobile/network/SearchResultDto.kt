package br.dc.ufscar.devmobile.network

data class SearchResultDto(
    val logo : String,
    val namepiece : String,
    val latitude : Float,
    val longitude : Float,
    val avgPrice : Float,
    val id : Int,
    var distance: Float? = null
)
