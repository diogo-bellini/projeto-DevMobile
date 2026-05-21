package br.dc.ufscar.devmobile.repositories

import br.dc.ufscar.devmobile.network.ReservationRequest
import br.dc.ufscar.devmobile.network.ReservationResponse
import br.dc.ufscar.devmobile.network.StoreApiService

class ReservationRepository(private val api: StoreApiService) {

    suspend fun createReservation(request: ReservationRequest): ReservationResponse =
        api.createReservation(request)
}
