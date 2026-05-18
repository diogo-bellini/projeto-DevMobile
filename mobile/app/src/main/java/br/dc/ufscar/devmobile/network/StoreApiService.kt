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
}
