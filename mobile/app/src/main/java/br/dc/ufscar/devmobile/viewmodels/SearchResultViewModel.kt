package br.dc.ufscar.devmobile.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dc.ufscar.devmobile.configs.LocationService
import br.dc.ufscar.devmobile.network.RestaurantSearchItemDto
import br.dc.ufscar.devmobile.network.RetrofitClient
import br.dc.ufscar.devmobile.network.SearchResultDto
import kotlinx.coroutines.launch

class SearchResultViewModel : ViewModel() {
    val resultsSearchBar = mutableStateListOf<RestaurantSearchItemDto>()
    val resultCategory = mutableStateListOf<SearchResultDto>()

    var userLatitude by mutableDoubleStateOf(0.0)
    var userLongitude by mutableDoubleStateOf(0.0)

    fun getLocation(context : Context) {
        LocationService.getCurrentLocation(
            context = context,
            onSuccess = { lat, long ->
                setLocation(lat, long)
            },
            onError = {}
        )
    }

    fun setLocation(lat : Double, long : Double){
        userLatitude = lat
        userLongitude = long
    }

    fun searchStoresBySubstring(text : String){
        if (text.isBlank()) {
            resultsSearchBar.clear()
            return
        }

        viewModelScope.launch {
            try {
                val stores = RetrofitClient.storeApi.getStoresBySubString(text)
                resultsSearchBar.clear()
                resultsSearchBar.addAll(
                    stores.map { store ->
                        RestaurantSearchItemDto(
                            namepiece = store.namepiece,
                            logoUrl = store.logo,
                            id = store.id
                        )
                    }
                )
            } catch (e: Exception) {
                resultsSearchBar.clear()
            }
        }
    }

    fun searchStoresByCategory(category : String){
        viewModelScope.launch {
            try {
                val stores = RetrofitClient.storeApi.getStoresByCategory(category)
                resultCategory.clear()
                val results = stores.map { store ->
                    SearchResultDto(
                        logo = store.logo,
                        namepiece = store.namepiece,
                        latitude = store.latitude,
                        longitude = store.longitude,
                        avgPrice = store.avgPrice,
                        id = store.id
                    )
                }

                results.forEach { store ->
                    store.distance = calculateDistance(
                        userLatitude, userLongitude,
                        store.latitude.toDouble(), store.longitude.toDouble()
                    )
                }
                resultCategory.addAll(results)
            } catch (e: Exception){
                resultCategory.clear()
            }
        }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0] / 1000 // Converter para km
    }
}