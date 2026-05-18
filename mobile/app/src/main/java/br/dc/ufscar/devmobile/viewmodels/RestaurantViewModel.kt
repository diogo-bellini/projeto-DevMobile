package br.dc.ufscar.devmobile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.dc.ufscar.devmobile.network.RetrofitClient
import br.dc.ufscar.devmobile.network.Store
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RestaurantViewModel(private val storeId: Int) : ViewModel() {
    private val _store = MutableStateFlow<Store?>(null)
    val store: StateFlow<Store?> = _store

    init {
        fetchStore()
    }

    private fun fetchStore() {
        viewModelScope.launch {
            runCatching { RetrofitClient.storeApi.getStore(storeId) }
                .onSuccess { _store.value = it }
        }
    }

    class Factory(private val storeId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return RestaurantViewModel(storeId) as T
        }
    }
}
