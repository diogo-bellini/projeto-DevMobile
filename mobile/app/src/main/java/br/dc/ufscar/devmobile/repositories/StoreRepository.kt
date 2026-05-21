package br.dc.ufscar.devmobile.repositories

import br.dc.ufscar.devmobile.network.RestaurantSearchItemDto
import br.dc.ufscar.devmobile.network.SearchResultDto
import br.dc.ufscar.devmobile.network.Store
import br.dc.ufscar.devmobile.network.StoreApiService

class StoreRepository(private val api: StoreApiService) {

    suspend fun getStores(): List<Store> = api.getStores()

    suspend fun getStore(id: Int): Store = api.getStore(id)

    suspend fun getStoresBySubString(text: String): List<RestaurantSearchItemDto> =
        api.getStoresBySubString(text).map { store ->
            RestaurantSearchItemDto(namepiece = store.namepiece, logoUrl = store.logo, id = store.id)
        }

    suspend fun getStoresByCategory(category: String): List<SearchResultDto> =
        api.getStoresByCategory(category)

    suspend fun getStoresByFilters(
        maxPrice: Float,
        minReviews: Int,
        maxReviews: Int,
        minRating: Int
    ): List<SearchResultDto> = api.getStoresByFilters(maxPrice, minReviews, maxReviews, minRating)

    suspend fun getStoresByFiltersWithCategory(
        category: String,
        maxPrice: Float,
        minReviews: Int,
        maxReviews: Int,
        minRating: Int
    ): List<SearchResultDto> =
        api.getStoresByFiltersWithCategory(category, maxPrice, minReviews, maxReviews, minRating)
}
