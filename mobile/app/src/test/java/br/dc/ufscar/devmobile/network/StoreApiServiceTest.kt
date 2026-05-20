package br.dc.ufscar.devmobile.network

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StoreApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: StoreApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StoreApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getStores returns list correctly`() = runTest {
        val json = """[{
            "id": 1,
            "name": "Burger Place",
            "namepiece": "Burger",
            "address": "Rua Teste, 123",
            "logo": "logo1.png",
            "rating": 4.5,
            "reviews": 100,
            "avgPrice": 35.0,
            "category": "Burger",
            "time": "30-45 min",
            "latitude": -22.0,
            "longitude": -47.0
        }]"""
        mockWebServer.enqueue(MockResponse().setBody(json).setResponseCode(200))

        val result = api.getStores()

        assertEquals(1, result.size)
        assertEquals("Burger Place", result[0].name)
        assertEquals("Burger", result[0].category)
    }

    @Test
    fun `createReservation sends correct body`() = runTest {
        val json = """{
            "id": 99,
            "storeId": 1,
            "date": "2026-06-01",
            "time": "19:00",
            "people": 2,
            "table": 3,
            "createdAt": "2026-05-20T10:00:00.000Z"
        }"""
        mockWebServer.enqueue(MockResponse().setBody(json).setResponseCode(201))

        val request = ReservationRequest(
            storeId = 1,
            date = "2026-06-01",
            time = "19:00",
            people = 2,
            table = 3,
            createdAt = "2026-05-20T10:00:00.000Z"
        )
        api.createReservation(request)

        val sent = mockWebServer.takeRequest()
        assertEquals("POST", sent.method)
        assert(sent.body.readUtf8().contains("\"storeId\":1"))
    }
}
