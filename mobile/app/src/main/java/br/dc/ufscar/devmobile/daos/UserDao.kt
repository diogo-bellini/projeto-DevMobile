package br.dc.ufscar.devmobile.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.dc.ufscar.devmobile.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): User?

    @Query("SELECT * FROM User LIMIT 1")
    fun getCurrent(): Flow<User?>

    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE FROM User")
    suspend fun deleteAll()
}