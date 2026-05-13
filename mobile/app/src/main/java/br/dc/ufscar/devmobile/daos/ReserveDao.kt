package br.dc.ufscar.devmobile.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import br.dc.ufscar.devmobile.entities.Reserve

@Dao
interface ReserveDao {
    @Insert
    suspend fun insert(reserve: Reserve)

    @Update
    suspend fun update(reserve: Reserve)

    @Delete
    suspend fun delete(reserve: Reserve)
}