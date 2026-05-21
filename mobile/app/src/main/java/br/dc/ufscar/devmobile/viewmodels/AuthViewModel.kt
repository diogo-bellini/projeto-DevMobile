package br.dc.ufscar.devmobile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.dc.ufscar.devmobile.daos.UserDao
import br.dc.ufscar.devmobile.entities.User
import br.dc.ufscar.devmobile.network.LoginRequest
import br.dc.ufscar.devmobile.network.RegisterRequest
import br.dc.ufscar.devmobile.network.RetrofitClient
import br.dc.ufscar.devmobile.network.UserResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: UserResponse) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(private val userDao: UserDao) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, senha: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = RetrofitClient.storeApi.login(LoginRequest(email, senha))
                saveUserLocally(user, senha)
                _authState.value = AuthState.Success(user)
            } catch (e: retrofit2.HttpException) {
                val msg = when (e.code()) {
                    401 -> "Email ou senha incorretos."
                    else -> "Erro ao fazer login. Tente novamente."
                }
                _authState.value = AuthState.Error(msg)
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Sem conexão com o servidor.")
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
                val user = RetrofitClient.storeApi.register(
                    RegisterRequest(nomeCompleto, email, cpf, dataNascimento, senha)
                )
                saveUserLocally(user, senha)
                _authState.value = AuthState.Success(user)
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Erro ao criar conta. Tente novamente.")
            }
        }
    }

    private suspend fun saveUserLocally(userResponse: UserResponse, password: String) {
        try {
            val birthdate = LocalDate.parse(userResponse.dataNascimento)
            val user = User(
                id = userResponse.id,
                fullName = userResponse.nomeCompleto,
                email = userResponse.email,
                cpf = userResponse.cpf,
                birthdate = birthdate,
                password = password
            )
            userDao.deleteAll()
            userDao.insert(user)
        } catch (_: Exception) {
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

class AuthViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}