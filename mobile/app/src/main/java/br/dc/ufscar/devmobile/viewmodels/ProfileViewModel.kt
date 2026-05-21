package br.dc.ufscar.devmobile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.dc.ufscar.devmobile.entities.User
import br.dc.ufscar.devmobile.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val user: User) : ProfileState()
    object Empty : ProfileState()
}

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val state: StateFlow<ProfileState> = _state

    init {
        loadUser()
    }

    fun loadUser() {
        viewModelScope.launch {
            userRepository.getCurrentUser().collect { user ->
                _state.value = if (user != null) ProfileState.Success(user) else ProfileState.Empty
            }
        }
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            userRepository.logout()
            onDone()
        }
    }
}

class ProfileViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
