package br.dc.ufscar.devmobile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dc.ufscar.devmobile.network.RetrofitClient
import br.dc.ufscar.devmobile.network.Store
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _stores = MutableStateFlow<List<Store>>(emptyList())
    val stores: StateFlow<List<Store>> = _stores

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchStores()
    }

    private fun fetchStores() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            runCatching { RetrofitClient.storeApi.getStores() }
                .onSuccess {
                    _stores.value = it
                    _isLoading.value = false
                }
                .onFailure { e ->
                    _error.value = e.message ?: "Erro desconhecido"
                    _isLoading.value = false
                }
        }
    }
}
