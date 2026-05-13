package br.dc.ufscar.devmobile.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id : Int,
    @ColumnInfo(name = "full_name") val fullName : String,
    @ColumnInfo(name = "email") val email : String,
    @ColumnInfo(name = "cpf") val cpf : String,
    @ColumnInfo(name = "birthdate") val birthdate : LocalDate,
    @ColumnInfo(name = "password") val password : String
)
