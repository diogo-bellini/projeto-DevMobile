package br.dc.ufscar.devmobile.repositories

import br.dc.ufscar.devmobile.network.MenuItemDto
import br.dc.ufscar.devmobile.network.StoreApiService

class MenuRepository(private val api: StoreApiService) {

    suspend fun getMenuItems(storeId: Int): List<MenuItemDto> = api.getMenuItems(storeId)
}
