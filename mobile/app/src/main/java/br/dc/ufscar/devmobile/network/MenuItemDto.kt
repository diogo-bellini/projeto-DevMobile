package br.dc.ufscar.devmobile.network

data class MenuItemDto(
    val id: Int,
    val storeId: Int,
    val category: String,
    val name: String,
    val description: String
)
