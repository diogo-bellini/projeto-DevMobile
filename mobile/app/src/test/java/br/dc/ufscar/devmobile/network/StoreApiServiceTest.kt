package br.dc.ufscar.devmobile.network

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StoreApiServiceTest {

    private lateinit var api: StoreApiService

    @Before
    fun setUp() {
        api = Retrofit.Builder()
            .baseUrl("http://localhost:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StoreApiService::class.java)
    }

    @Test
    fun `getStores returns non-empty list from real API`() = runTest {
        val result = api.getStores()

        assertTrue("API deve retornar ao menos uma loja", result.isNotEmpty())
        assertNotNull(result[0].name)
    }

    @Test
    fun `getStore returns correct store by id`() = runTest {
        val result = api.getStore(1)

        assertNotNull(result)
        assertTrue(result.id == 1)
        assertNotNull(result.name)
    }

    @Test
    fun `createReservation persists and returns reservation with id`() = runTest {
        val request = ReservationRequest(
            storeId = 1,
            date = "2026-06-01",
            time = "19:00",
            people = 2,
            table = 3,
            createdAt = "2026-06-01T19:00:00.000Z"
        )

        val result = api.createReservation(request)

        assertNotNull(result)
        assertTrue(result.id > 0)
        assertTrue(result.storeId == 1)
    }
}
