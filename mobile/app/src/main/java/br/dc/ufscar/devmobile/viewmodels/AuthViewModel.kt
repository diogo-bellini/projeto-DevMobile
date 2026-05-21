package br.dc.ufscar.devmobile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.dc.ufscar.devmobile.network.UserResponse
import br.dc.ufscar.devmobile.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: UserResponse) : AuthState()
    data class Error(val message: String? = null, val messageResId: Int? = null) : AuthState()
}

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, senha: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = userRepository.login(email, senha)
                _authState.value = AuthState.Success(user)
            } catch (e: retrofit2.HttpException) {
                val resId = when (e.code()) {
                    401 -> br.dc.ufscar.devmobile.R.string.error_login_invalid_credentials
                    else -> br.dc.ufscar.devmobile.R.string.error_login_generic
                }
                _authState.value = AuthState.Error(messageResId = resId)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(messageResId = br.dc.ufscar.devmobile.R.string.error_no_connection)
            }
        }
    }

    fun register(
        nomeCompleto: String,
        email: String,
        cpf: String,
        dataNascimento: String,
        senha: String
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = userRepository.register(nomeCompleto, email, cpf, dataNascimento, senha)
                _authState.value = AuthState.Success(user)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(messageResId = br.dc.ufscar.devmobile.R.string.error_register_generic)
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

class AuthViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
