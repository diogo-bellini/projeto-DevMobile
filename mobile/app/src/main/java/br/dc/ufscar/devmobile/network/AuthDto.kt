package br.dc.ufscar.devmobile.network

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("senha") val senha: String
)

data class RegisterRequest(
    @SerializedName("nomeCompleto") val nomeCompleto: String,
    @SerializedName("email") val email: String,
    @SerializedName("cpf") val cpf: String,
    @SerializedName("dataNascimento") val dataNascimento: String,
    @SerializedName("senha") val senha: String
)

data class UserResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("nomeCompleto") val nomeCompleto: String,
    @SerializedName("email") val email: String,
    @SerializedName("cpf") val cpf: String,
    @SerializedName("dataNascimento") val dataNascimento: String
)