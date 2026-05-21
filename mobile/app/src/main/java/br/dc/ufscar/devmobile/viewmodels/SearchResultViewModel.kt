package br.dc.ufscar.devmobile.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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
    val otherResults = mutableStateListOf<SearchResultDto>()

    var isLoading by mutableStateOf(false)
    var userLatitude by mutableDoubleStateOf(0.0)
    var userLongitude by mutableDoubleStateOf(0.0)

    fun getLocation(context : Context, onFinished : () -> Unit = {}) {
        isLoading = true
        LocationService.getCurrentLocation(
            context = context,
            onSuccess = { lat, long ->
                setLocation(lat, long)
                onFinished()
            },
            onError = {
                onFinished()
            }
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
        isLoading = true
        viewModelScope.launch {
            try {
                val stores = RetrofitClient.storeApi.getStoresByCategory(category)
                otherResults.clear()

                val results = stores.map { store ->
                    store.copy(
                        distance = calculateDistance(
                            userLatitude, userLongitude,
                            store.latitude.toDouble(), store.longitude.toDouble()
                        )
                    )
                }
                otherResults.addAll(results)
            } catch (e: Exception){
                otherResults.clear()
            } finally {
                isLoading = false
            }
        }
    }

    fun searchStoresByFilters(category: String?, price : Float, distance : Float, minReviews : Int, maxReviews : Int, minRating : Int){
        isLoading = true
        viewModelScope.launch {
            if (category != null) {
                try {
                    val stores = RetrofitClient.storeApi.getStoresByFiltersWithCategory(
                        maxPrice = price,
                        minReviews = minReviews,
                        maxReviews = maxReviews,
                        minRating = minRating,
                        category = category
                    )
                    otherResults.clear()

                    val filteredResults = stores.mapNotNull { store ->
                        val calculatedDist = calculateDistance(
                            userLatitude, userLongitude,
                            store.latitude.toDouble(), store.longitude.toDouble()
                        )

                        if (calculatedDist <= distance) {
                            store.copy(distance = calculatedDist)
                        } else {
                            null
                        }
                    }
                    otherResults.addAll(filteredResults)
                } catch (e : Exception){
                    otherResults.clear()
                } finally {
                    isLoading = false
                }
            } else {
                try {
                    val stores = RetrofitClient.storeApi.getStoresByFilters(
                        maxPrice = price,
                        minReviews = minReviews,
                        maxReviews = maxReviews,
                        minRating = minRating,
                    )
                    otherResults.clear()

                    val filteredResults = stores.mapNotNull { store ->
                        val calculatedDist = calculateDistance(
                            userLatitude, userLongitude,
                            store.latitude.toDouble(), store.longitude.toDouble()
                        )

                        if (calculatedDist <= distance) {
                            store.copy(distance = calculatedDist)
                        } else {
                            null
                        }
                    }
                    otherResults.addAll(filteredResults)
                } catch (e : Exception){
                    otherResults.clear()
                } finally {
                    isLoading = false
                }
            }
        }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0] / 1000 // Converter para km
    }
}
