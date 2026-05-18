package br.dc.ufscar.devmobile.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Reserve(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    @ColumnInfo(name = "date") val date : LocalDate,
    @ColumnInfo(name = "user_id") val userId : Int
)
