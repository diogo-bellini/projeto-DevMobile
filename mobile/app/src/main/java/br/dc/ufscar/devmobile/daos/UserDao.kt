package br.dc.ufscar.devmobile.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.dc.ufscar.devmobile.entities.User

@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE id = :id LIMIT 1")
    suspend fun getById(id : Int) : User?

    @Insert
    suspend fun insert(user : User)

    @Update
    suspend fun update(user : User)

    @Delete
    suspend fun delete(user : User)
}