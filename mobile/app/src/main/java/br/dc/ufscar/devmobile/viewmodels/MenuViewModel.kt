package br.dc.ufscar.devmobile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.dc.ufscar.devmobile.network.RetrofitClient
import br.dc.ufscar.devmobile.views.MenuItem
import br.dc.ufscar.devmobile.views.MenuSection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MenuViewModel(private val storeId: Int) : ViewModel() {
    private val _sections = MutableStateFlow<List<MenuSection>>(emptyList())
    val sections: StateFlow<List<MenuSection>> = _sections

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchMenu()
    }

    private fun fetchMenu() {
        viewModelScope.launch {
            runCatching { RetrofitClient.storeApi.getMenuItems(storeId) }
                .onSuccess { items ->
                    _sections.value = items
                        .groupBy { it.category }
                        .map { (category, grouped) ->
                            MenuSection(
                                title = category,
                                items = grouped.map { MenuItem(it.name, it.description) }
                            )
                        }
                    _isLoading.value = false
                }
                .onFailure { _isLoading.value = false }
        }
    }

    class Factory(private val storeId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MenuViewModel(storeId) as T
        }
    }
}
