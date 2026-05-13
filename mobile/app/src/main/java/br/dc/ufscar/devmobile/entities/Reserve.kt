package br.dc.ufscar.devmobile.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Reserve(
    @PrimaryKey(autoGenerate = true) val id : Int,
    @ColumnInfo(name = "date") val date : LocalDate,
    @ColumnInfo(name = "user") val user : User
)
