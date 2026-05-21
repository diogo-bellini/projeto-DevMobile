package br.dc.ufscar.devmobile.repositories

import br.dc.ufscar.devmobile.daos.UserDao
import br.dc.ufscar.devmobile.entities.User
import br.dc.ufscar.devmobile.network.LoginRequest
import br.dc.ufscar.devmobile.network.RegisterRequest
import br.dc.ufscar.devmobile.network.StoreApiService
import br.dc.ufscar.devmobile.network.UserResponse
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class UserRepository(
    private val userDao: UserDao,
    private val api: StoreApiService
) {
    fun getCurrentUser(): Flow<User?> = userDao.getCurrent()

    suspend fun login(email: String, senha: String): UserResponse {
        val response = api.login(LoginRequest(email, senha))
        persistUser(response, senha)
        return response
    }

    suspend fun register(
        nomeCompleto: String,
        email: String,
        cpf: String,
        dataNascimento: String,
        senha: String
    ): UserResponse {
        val response = api.register(RegisterRequest(nomeCompleto, email, cpf, dataNascimento, senha))
        persistUser(response, senha)
        return response
    }

    suspend fun logout() = userDao.deleteAll()

    private suspend fun persistUser(response: UserResponse, password: String) {
        val user = User(
            id = response.id,
            fullName = response.nomeCompleto,
            email = response.email,
            cpf = response.cpf,
            birthdate = LocalDate.parse(response.dataNascimento),
            password = password
        )
        userDao.deleteAll()
        userDao.insert(user)
    }
}
