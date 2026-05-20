package br.dc.ufscar.devmobile.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface StoreApiService {
    @GET("stores")
    suspend fun getStores(): List<Store>

    @GET("stores/{id}")
    suspend fun getStore(@Path("id") id: Int): Store

    @GET("menuItems")
    suspend fun getMenuItems(@Query("storeId") storeId: Int): List<MenuItemDto>

    @POST("reservations")
    suspend fun createReservation(@Body request: ReservationRequest): ReservationResponse

    @GET("stores")
    suspend fun getStoresByCategory(@Query("category") category : String) : List<SearchResultDto>

    @GET("stores")
    suspend fun getStoresBySubString(@Query("namepiece_like") substring : String) : List<Store>
    
    @GET("stores")
    suspend fun getStoresByFilters(
        @Query("avgPrice_lte") maxPrice: Float,
        @Query("reviews_gte") minReviews: Int,
        @Query("reviews_lte") maxReviews: Int,
        @Query("rating_gte") minRating: Int,
    ) : List<SearchResultDto>

    @GET("stores")
    suspend fun getStoresByFiltersWithCategory(
        @Query("category") category: String?,
        @Query("avgPrice_lte") maxPrice: Float,
        @Query("reviews_gte") minReviews: Int,
        @Query("reviews_lte") maxReviews: Int,
        @Query("rating_gte") minRating: Int,
    ) : List<SearchResultDto>
}
