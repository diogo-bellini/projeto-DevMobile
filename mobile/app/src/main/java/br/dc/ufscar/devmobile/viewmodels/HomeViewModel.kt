package br.dc.ufscar.devmobile.viewmodels

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.dc.ufscar.devmobile.R
import br.dc.ufscar.devmobile.configs.LocationService
import br.dc.ufscar.devmobile.network.Store
import br.dc.ufscar.devmobile.repositories.StoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class HomeViewModel(private val storeRepository: StoreRepository) : ViewModel() {
    private val _stores = MutableStateFlow<List<Store>>(emptyList())
    val stores: StateFlow<List<Store>> = _stores

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _errorRes = MutableStateFlow<Int?>(null)
    val errorRes: StateFlow<Int?> = _errorRes

    private val _locationNameRes = MutableStateFlow<Int?>(R.string.home_location_undefined)
    val locationNameRes: StateFlow<Int?> = _locationNameRes

    private val _locationName = MutableStateFlow<String?>(null)
    val locationName: StateFlow<String?> = _locationName

    init {
        fetchStores()
    }

    fun updateLocation(context: Context) {
        LocationService.getCurrentLocation(
            context = context,
            onSuccess = { lat, lon ->
                viewModelScope.launch {
                    val result = getAddressFromCoords(context, lat, lon)
                    if (result is Int) {
                        _locationNameRes.value = result
                        _locationName.value = null
                    } else {
                        _locationName.value = result as String
                        _locationNameRes.value = null
                    }
                }
            },
            onError = { _ ->
                _locationNameRes.value = R.string.home_location_unavailable
                _locationName.value = null
            }
        )
    }

    private fun getAddressFromCoords(context: Context, lat: Double, lon: Double): Any {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (!addresses.isNullOrEmpty()) {
                val addr = addresses[0]
                addr.thoroughfare ?: addr.subLocality ?: addr.locality ?: R.string.home_location_unknown
            } else {
                R.string.home_location_not_found
            }
        } catch (e: Exception) {
            R.string.home_location_error
        }
    }

    private fun fetchStores() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _errorRes.value = null
            runCatching { storeRepository.getStores() }
                .onSuccess {
                    _stores.value = it
                    _isLoading.value = false
                }
                .onFailure { e ->
                    _error.value = e.message
                    if (e.message == null) {
                        _errorRes.value = R.string.error_unknown
                    }
                    _isLoading.value = false
                }
        }
    }

    class Factory(private val storeRepository: StoreRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(storeRepository) as T
        }
    }
}
