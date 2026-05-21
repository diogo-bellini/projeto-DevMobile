package br.dc.ufscar.devmobile.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.dc.ufscar.devmobile.configs.LocationService
import br.dc.ufscar.devmobile.network.RestaurantSearchItemDto
import br.dc.ufscar.devmobile.network.SearchResultDto
import br.dc.ufscar.devmobile.repositories.StoreRepository
import kotlinx.coroutines.launch

class SearchResultViewModel(private val storeRepository: StoreRepository) : ViewModel() {
    val resultsSearchBar = mutableStateListOf<RestaurantSearchItemDto>()
    val otherResults = mutableStateListOf<SearchResultDto>()

    var isLoading by mutableStateOf(false)
    var userLatitude by mutableDoubleStateOf(0.0)
    var userLongitude by mutableDoubleStateOf(0.0)

    fun getLocation(context: Context, onFinished: () -> Unit = {}) {
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

    fun setLocation(lat: Double, long: Double) {
        userLatitude = lat
        userLongitude = long
    }

    fun searchStoresBySubstring(text: String) {
        if (text.isBlank()) {
            resultsSearchBar.clear()
            return
        }

        viewModelScope.launch {
            try {
                val stores = storeRepository.getStoresBySubString(text)
                resultsSearchBar.clear()
                resultsSearchBar.addAll(stores)
            } catch (e: Exception) {
                resultsSearchBar.clear()
            }
        }
    }

    fun searchStoresByCategory(category: String) {
        isLoading = true
        viewModelScope.launch {
            try {
                val stores = storeRepository.getStoresByCategory(category)
                otherResults.clear()
                otherResults.addAll(stores.map { store ->
                    store.copy(
                        distance = calculateDistance(
                            userLatitude, userLongitude,
                            store.latitude.toDouble(), store.longitude.toDouble()
                        )
                    )
                })
            } catch (e: Exception) {
                otherResults.clear()
            } finally {
                isLoading = false
            }
        }
    }

    fun searchStoresByFilters(
        category: String?,
        price: Float,
        distance: Float,
        minReviews: Int,
        maxReviews: Int,
        minRating: Int
    ) {
        isLoading = true
        viewModelScope.launch {
            try {
                val stores = if (category != null) {
                    storeRepository.getStoresByFiltersWithCategory(
                        category, price, minReviews, maxReviews, minRating
                    )
                } else {
                    storeRepository.getStoresByFilters(price, minReviews, maxReviews, minRating)
                }
                otherResults.clear()
                otherResults.addAll(stores.mapNotNull { store ->
                    val calculatedDist = calculateDistance(
                        userLatitude, userLongitude,
                        store.latitude.toDouble(), store.longitude.toDouble()
                    )
                    if (calculatedDist <= distance) store.copy(distance = calculatedDist) else null
                })
            } catch (e: Exception) {
                otherResults.clear()
            } finally {
                isLoading = false
            }
        }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0] / 1000
    }

    class Factory(private val storeRepository: StoreRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return SearchResultViewModel(storeRepository) as T
        }
    }
}
