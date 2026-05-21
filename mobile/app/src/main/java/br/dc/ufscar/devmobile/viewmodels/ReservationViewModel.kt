package br.dc.ufscar.devmobile.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.dc.ufscar.devmobile.network.ReservationRequest
import br.dc.ufscar.devmobile.network.Store
import br.dc.ufscar.devmobile.repositories.ReservationRepository
import br.dc.ufscar.devmobile.repositories.StoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class ReservationUiState {
    object Idle : ReservationUiState()
    object Loading : ReservationUiState()
    object Success : ReservationUiState()
    data class Error(val message: String) : ReservationUiState()
}

class ReservationViewModel(
    private val storeId: Int,
    private val storeRepository: StoreRepository,
    private val reservationRepository: ReservationRepository
) : ViewModel() {

    private val _store = MutableStateFlow<Store?>(null)
    val store: StateFlow<Store?> = _store

    var people by mutableIntStateOf(0)
    var selectedDateMillis by mutableStateOf<Long?>(null)
    var selectedTime by mutableStateOf("")
    var selectedTable by mutableIntStateOf(0)

    private val _uiState = MutableStateFlow<ReservationUiState>(ReservationUiState.Idle)
    val uiState: StateFlow<ReservationUiState> = _uiState

    val isFormValid get() = people > 0 && selectedDateMillis != null && selectedTime.isNotEmpty() && selectedTable > 0

    init {
        fetchStore()
    }

    private fun fetchStore() {
        viewModelScope.launch {
            runCatching { storeRepository.getStore(storeId) }
                .onSuccess { _store.value = it }
        }
    }

    fun createReservation() {
        val millis = selectedDateMillis ?: return
        if (people <= 0 || selectedTime.isEmpty()) return

        val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(millis))
        val createdAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(Date())

        viewModelScope.launch {
            _uiState.value = ReservationUiState.Loading
            runCatching {
                reservationRepository.createReservation(
                    ReservationRequest(
                        storeId = storeId,
                        date = dateStr,
                        time = selectedTime,
                        people = people,
                        table = selectedTable,
                        createdAt = createdAt
                    )
                )
            }
                .onSuccess { _uiState.value = ReservationUiState.Success }
                .onFailure { _uiState.value = ReservationUiState.Error(it.message ?: "Erro desconhecido") }
        }
    }

    class Factory(
        private val storeId: Int,
        private val storeRepository: StoreRepository,
        private val reservationRepository: ReservationRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ReservationViewModel(storeId, storeRepository, reservationRepository) as T
        }
    }
}
